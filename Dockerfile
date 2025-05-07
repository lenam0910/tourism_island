# Sử dụng image OpenJDK 17 làm base
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file Maven wrapper và file cấu hình Maven
COPY mvnw .
COPY .mvn .mvn

# Cấp quyền thực thi cho mvnw
RUN chmod +x mvnw

# Sao chép file pom.xml và tải các phụ thuộc
COPY pom.xml .
RUN ./mvnw dependency:go-offline

# Sao chép mã nguồn của dự án
COPY src src

# Build ứng dụng Spring Boot
RUN ./mvnw package -DskipTests

# Expose port mà ứng dụng Spring Boot sử dụng (mặc định 8080)
EXPOSE 8080

# Lệnh chạy ứng dụng Spring Boot
CMD ["java", "-jar", "target/beautifulweb-0.0.1-SNAPSHOT.jar"]