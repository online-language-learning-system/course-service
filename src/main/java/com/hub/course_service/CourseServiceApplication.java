package com.hub.course_service;

import com.hub.course_service.grpc.CourseServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.hub.course_service.feignclient")
public class CourseServiceApplication {

	public static void main(String[] args)
			throws IOException, InterruptedException {

		ConfigurableApplicationContext context = SpringApplication.run(CourseServiceApplication.class, args);

		CourseServiceImpl courseServiceImpl = context.getBean(CourseServiceImpl.class);

		Server server = ServerBuilder.forPort(50051)
				.addService(courseServiceImpl)
				.build()
				.start();

		System.out.println("Server started on port 50051");
		server.awaitTermination();

	}

}
