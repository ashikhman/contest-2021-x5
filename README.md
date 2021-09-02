# Building the image

    ./gradlew clean build -x test && \
        docker build -t stor.highloadcup.ru/perfect_store/able_armadillo .

# Push the image

    ./gradlew clean build -x test && \
        docker build -t stor.highloadcup.ru/perfect_store/able_armadillo . && \
        docker push stor.highloadcup.ru/perfect_store/able_armadillo

# Testing the image

    docker run -e rs.endpoint=http://localhost:8080 stor.highloadcup.ru/perfect_store/able_armadillo
