package com.example.quickstart

import cats.effect.Async
import cats.syntax.all.*
import com.comcast.ip4s.*
import fs2.io.net.Network
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.headers.`Content-Type`
import org.http4s.MediaType
import scala.io.Source
import scala.util.Try

object QuickstartServer:

  def run[F[_]: Async: Network]: F[Nothing] = {
    val dsl = new Http4sDsl[F]{}
    import dsl.*

    for {
      client <- EmberClientBuilder.default[F].build
      helloWorldAlg = HelloWorld.impl[F]
      jokeAlg = Jokes.impl[F](client)

      // Create home route that serves index.html
      homeRoute = HttpRoutes.of[F] {
        case GET -> Root =>
          val html = Source.fromResource("index.html").mkString
          Ok(html, `Content-Type`(MediaType.text.html))
      }

      // Combine Service Routes into an HttpApp
      httpApp = (
        homeRoute <+>
        QuickstartRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        QuickstartRoutes.jokeRoutes[F](jokeAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      // Get port from environment variable or default to 8080
      port = Try(sys.env.get("PORT").map(_.toInt).getOrElse(8080)).getOrElse(8080)
      serverPort = Port.fromInt(port).getOrElse(port"8080")

      _ <-
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(serverPort)
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
