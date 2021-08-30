# Building the image

    ./gradlew build -x test
        docker build -t aga02 .

# Testing the image

    docker run -e rs.endpoint=http://localhost:8080 aga02
