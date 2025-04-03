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

object QuickstartServer:

  def run[F[_]: Async: Network]: F[Nothing] = {
    val dsl = new Http4sDsl[F]{}
    import dsl.*

    for {
      client <- EmberClientBuilder.default[F].build
      helloWorldAlg = HelloWorld.impl[F]
      jokeAlg = Jokes.impl[F](client)

      // Create home route that uses HelloWorld service
      homeRoute = HttpRoutes.of[F] {
        case GET -> Root =>
          for {
            greeting <- helloWorldAlg.hello(HelloWorld.Name("World"))
            resp <- Ok(greeting)
          } yield resp
      }

      // Combine Service Routes into an HttpApp
      httpApp = (
        homeRoute <+>
        QuickstartRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        QuickstartRoutes.jokeRoutes[F](jokeAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      _ <-
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
