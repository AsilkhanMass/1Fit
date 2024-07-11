FROM openjdk:17

WORKDIR /app

# .mvn katalogini barcha fayllarni ko'chirish
COPY .mvn .mvn

# Maven Wrapper-ni barcha fayllarni ko'chirish
COPY mvnw .

# Faylni ishga tushirish uchun execute ruxsat bering
RUN chmod +x mvnw

# Dependenciesni yuklash
RUN ./mvnw dependency:go-offline

# Boshqa o'zgaruvchilarni qo'shish kerak bo'lsa, bu erda amalga oshiring

# App kodlari ko'chirish
COPY . .

# Service ishga tushirish
CMD ["./mvnw", "spring-boot:run"]
