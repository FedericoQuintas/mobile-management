From openjdk:17

COPY build/libs/mobile-management-0.0.1.jar mobile-management-0.0.1.jar

ENTRYPOINT ["java","-jar","mobile-management-0.0.1.jar"]
