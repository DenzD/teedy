FROM 192.168.0.143:8085/teedy/maven:1.1 AS base

COPY . .
RUN mvn -Pprod -DskipTests clean install

FROM sismics/ubuntu-jetty:9.4.36 AS stage1

RUN apt-get update && \
    apt-get -y -q --no-install-recommends install \
    ffmpeg \
    mediainfo \
    tesseract-ocr \
    tesseract-ocr-ara \
    tesseract-ocr-chi-sim \
    tesseract-ocr-chi-tra \
    tesseract-ocr-dan \
    tesseract-ocr-deu \
    tesseract-ocr-fin \
    tesseract-ocr-fra \
    tesseract-ocr-heb \
    tesseract-ocr-hin \
    tesseract-ocr-hun \
    tesseract-ocr-ita \
    tesseract-ocr-jpn \
    tesseract-ocr-kor \
    tesseract-ocr-lav \
    tesseract-ocr-nld \
    tesseract-ocr-nor \
    tesseract-ocr-pol \
    tesseract-ocr-por \
    tesseract-ocr-rus \
    tesseract-ocr-spa \
    tesseract-ocr-swe \
    tesseract-ocr-tha \
    tesseract-ocr-tur \
    tesseract-ocr-ukr \
    tesseract-ocr-vie && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Remove the embedded javax.mail jar from Jetty
RUN rm -f /opt/jetty/lib/mail/javax.mail.glassfish-*.jar

COPY --from=base /docs-web/target/docs-web-1.10.war /opt/jetty/webapps/docs-web-1.10.war

ENV JAVA_OPTIONS -Xmx1g
