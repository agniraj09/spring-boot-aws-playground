# spring-boot-aws-playground
Playground repository to integrate AWS in spring boot project and try out various features offered by AWS


Sample File Upload Request
---
>curl --location 'http://localhost:8080/file/upload' \
--form 'fileName="sample-word-file.docx"' \
--form 'file=@"/C:/Users/Downloads/CertInstallation.docx"'

Sample File Download Request
---
>http://localhost:8080/file/download?partner-name=quickplace-excellence-application&fileName=sample-word-file.docx