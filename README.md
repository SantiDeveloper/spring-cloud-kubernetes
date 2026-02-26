# Microservices with Docker and Databases
## Setup Instructions
#### This guide will help you set up a microservices architecture using Docker, PostgreSQL, and MySQL databases.
## Prerequisites
#### Ensure you have Docker installed on your machine.
### Step 1: Create a Docker Network
#### Create a custom Docker network to allow communication between containers.
```bash
docker network create spring
```
### Step 2: Create Docker Volumes for Data Persistence
#### Create Docker volumes to persist data for PostgreSQL and MySQL databases.
```bash
docker volume create data-postgres
docker volume create data-mysql
```
### Step 3: Run Database Containers
#### Run PostgreSQL and MySQL containers with the necessary environment variables and volume mappings.
```bash
docker pull postgres:14-alpine
docker pull mysql:8
docker run -d -p 5532:5432 --name postgres14 --network spring -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=ms_grades -v data-postgres:/var/lib/postgresql/data postgres:14-alpine
docker run -d -p 3307:3306 --name mysql8 --network spring -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=ms_users -v data-mysql:/var/lib/mysql mysql:8
```
## Step 4: Build and Run Microservice Containers
#### Build and run the microservice containers for grades and users.
```bash
docker build -t grades . -f .\ms-grades\Dockerfile 
docker run -d -p 8002:8002 --env-file .\ms-grades\.env --rm --name ms-grades --network spring grades
docker build -t users . -f .\ms-users\Dockerfile
docker run -d -p 8001:8001 --env-file .\ms-users\.env --rm --name ms-users --network spring users
```
## Step 5: Verify Setup
#### Check if all containers are running correctly.
```bash
docker ps
```
## Step 6: Access the Microservices
#### You can now access the microservices via the following URLs:
#### - Grades Microservice: `http://localhost:8002`
#### - Users Microservice: `http://localhost:8001`

## docker-compose.yml
#### Docker Compose configuration for microservices with PostgreSQL and MySQL databases.
#### This setup includes two microservices: ms-grades and ms-users.
#### - Para levantar los servicios, usa el comando: 
``` docker-compose up -d ```
#### - Para eliminar los servicios, usa el comando: 
```docker-compose down ```
#### - Para ver los logs de los servicios, usa el comando: 
```docker-compose logs -f ```
#### - Para listar los contenedores en ejecución, usa el comando: 
```docker-compose ps ```
#### - Para reconstruir las imágenes,  usa el comando: 
```docker-compose build ```
#### - Para detener los servicios,  usa el comando: 
```docker-compose stop ```
#### - Para iniciar los servicios detenidos, usa el comando: 
```docker-compose start ```
#### - Para ver los volúmenes creados, usa el comando: 
```docker volume ls ```
#### - Para eliminar los volúmenes creados, usa el comando: 
```docker volume rm <volume_name> ```
#### - Para ver las redes creadas, usa el comando: 
```docker network ls ```
#### - Para eliminar la red creada, usa el comando: 
```docker network rm spring ```
#### - Para ver los detalles de un contenedor específico, usa el comando: 
```docker inspect <container_name> ```
#### - Para acceder a la terminal de un contenedor en ejecución, usa el comando: 
```docker exec -it <container_name> /bin/sh ```
#### - Para ver el uso de recursos de los contenedores, usa el comando: 
```docker stats ```
#### - Para actualizar las imágenes de los servicios, usa el comando: 
```docker-compose pull ```
#### - Para ver el historial de cambios en las imágenes, usa el comando: 
```docker history <image_name> ```
#### - Para eliminar imágenes no utilizadas, usa el comando: 
```docker image prune ```
#### - Para eliminar contenedores detenidos, usa el comando: 
```docker container prune ```
#### - Para eliminar redes no utilizadas, usa el comando: 
```docker network prune ```
#### - Para eliminar todos los recursos no utilizados (contenedores, redes, imágenes, volúmenes), usa el comando: 
```docker system prune -a --volumes ```

# Docker Hub
#### docker hub es para subir las imagenes a la nube y que esten disponibles para otros usuarios o para despliegue en otros entornos.
#### Para subir una imagen a docker hub, primero debes iniciar sesión con el comando: 
``` docker login ```
#### Luego, etiqueta la imagen con tu nombre de usuario y el nombre del repositorio: 
```docker tag <image_name> <username>/<repository_name>:<tag> ```
#### Finalmente, sube la imagen con el comando: 
```docker push <username>/<repository_name>:<tag> ```
#### Para descargar una imagen desde docker hub, usa el comando: 
```docker pull <username>/<repository_name>:<tag> ```
#### Asegúrate de reemplazar <username>, <repository_name>, y <tag> con los valores correspondientes.
#### Puedes utilizar imagenes desde tu docker hub en el docker-compose.yml simplemente especificando la imagen en lugar de construirla localmente.
#### Por ejemplo:
```yaml
 services:
  ms-grades:
   image: <username>/<repository_name>:<tag>
   ports:
    - "8002:8002"
   env_file:
    - ./ms-grades/.env
```
#### Además, puedes automatizar el proceso de construcción y subida de imágenes a Docker Hub utilizando scripts o herramientas de integración continua (CI) como GitHub Actions, Jenkins, o GitLab CI/CD.
#### Asegúrate de mantener tus imágenes actualizadas y seguras siguiendo las mejores prácticas de Docker y gestión de contenedores.

## AWS EC2 Deployment
#### Ahora vamos a crear una EC2 en AWS para desplegar nuestros servicios dockerizados.
#### Primero, inicia sesión en tu cuenta de AWS y navega a la consola de EC2.
#### Haz clic en "Launch Instance" para crear una nueva instancia.
#### Selecciona una Amazon Machine Image (AMI) adecuada, como Amazon Linux 2 o Ubuntu.
#### Elige un tipo de instancia según tus necesidades (t2.micro es suficiente para pruebas).
#### Configura los detalles de la instancia, como la red y el almacenamiento.
#### En la sección "Configure Security Group", crea un nuevo grupo de seguridad o selecciona uno existente. Asegúrate de permitir el tráfico entrante en los puertos necesarios (8001, 8002, 5532, 3307).
#### Revisa y lanza la instancia. Descarga la clave PEM si es necesario para acceder a la instancia.
#### Para conectar con el cliente Putty necesitas convertir la clave PEM a PPK usando Puttygen.
#### Una vez que la instancia esté en ejecución, conéctate a ella usando SSH.
```bash
 ssh -i "your-key.pem" ec2-user@your-ec2-public-dns
```
#### Actualiza los paquetes e instala Docker.
```bash
 sudo dnf update -y
 sudo dnf install docker -y
 sudo systemctl start docker
 sudo systemctl enable docker
```
#### Instala Docker Compose.
``` curl -SL https://github.com/docker/compose/releases/download/v5.0.1/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose```
####  sudo chmod +x /usr/local/bin/docker-compose
####  o utiliza este comando para instalar:
```sudo dnf install docker-compose-plugin -y```
#### Verifica la instalación de Docker Compose.
###```bash
 docker-compose --version
###```
#### Si el comando anterior no funciona, crea un enlace simbólico:
```sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose```

#### Sube tu archivo docker-compose.yml y los archivos necesarios a la instancia EC2 usando SCP o cualquier otro método.
```bash
 scp -i "your-key.pem" docker-compose.yml ec2-user@your-ec2-public-dns:~
```
#### Para ello primero debes ajustar los permisos del archivo PEM en Windows usando los siguientes comandos en CMD:
#### :: 1. Quitar la herencia de permisos
``` icacls "spring-cloud.pem" /inheritance:r```

#### :: 2. Dar permiso total solo a tu usuario
``` icacls "spring-cloud.pem" /grant:r %USERNAME%:F```
#### Subir el archivo docker-compose.yml a la instancia EC2:
``` scp -F NUL -i "spring-cloud.pem" docker-compose.yaml ec2-user@ec2-18-117-141-173.us-east-2.compute.amazonaws.com:/home/ec2-user```
#### Navega al directorio donde subiste el archivo docker-compose.yml.
#### levantar los servicios usando Docker Compose.
```bash
 docker-compose up -d
```

## Ahora vamos a trabajar con ECS en AWS para desplegar nuestros servicios dockerizados.
#### Primero, inicia sesión en tu cuenta de AWS y navega a la consola de ECS.
#### Crea un nuevo clúster seleccionando "Networking only" para usar Fargate.
#### Define una nueva tarea (Task Definition) seleccionando Fargate como tipo de lanzamiento.
#### Nombre de la tarea: por ejemplo el nombre del contenedor de nuestro docker-compose.yml
#### Configuramos el rol de ejecución de la tarea (Task Execution Role) para permitir que ECS pueda descargar las imágenes de Docker.
#### AWSServiceRoleForECS sería para este ejemplo.
#### Caso de uso Elastic Container Service (ECS) para ejecutar aplicaciones en contenedores sin necesidad de gestionar servidores.
#### Eslastic Container Service Task Execution Role permite a ECS interactuar con otros servicios de AWS en nombre de la tarea.
#### AmazonECSTaskExecutionRolePolicy proporciona los permisos necesarios para que ECS pueda descargar imágenes de contenedores y enviar logs a CloudWatch.
``` network mode: awsvpc```
``` sistema operativo: Linux ```
``` task size: 1024 MB memory, 0.5 vCPU ```
#### Añade contenedores a la definición de tarea.
### Contenedor ms-users:
#### - Nombre: ms-users
#### - Imagen: <tu-usuario-docker-hub>/ms-users:latest
#### - Memoria limit: 256 MB
#### - Puerto: 8001
#### - Variables de entorno: Añade las variables necesarias para la conexión a la base de datos MySQL.
#### Importate para la comunicación entre contenedores dentro de la misma ECS, vamos por localhost en las variables de entorno.
#### Para conectar con otros ECS hay que ir por el DNS público del load balancer o por la ip pública asignada.
#### - Logging: Configura el logging para enviar los logs a CloudWatch.
### Contenedor mysql:
#### - Nombre: mysql
#### - Imagen: mysql:8
#### - Memoria limit: 512 MB
#### - Puerto: 3306
#### - Variables de entorno: Añade las variables necesarias para la configuración de MySQL.
#### - Logging: Configura el logging para enviar los logs a CloudWatch.
#### Orden de depliegue: Primero el contenedor de la base de datos (mysql) y luego el contenedor del microservicio (ms-users).
#### Eso se incluye en la propia configuración de la ECS.
### Contenedor ms-grades:
#### - Nombre: ms-grades
#### - Imagen: <tu-usuario-docker-hub>/ms-grades:latest
#### - Memoria limit: 256 MB
#### - Puerto: 8002
#### - Variables de entorno: Añade las variables necesarias para la conexión a la base de datos PostgreSQL.
#### - Logging: Configura el logging para enviar los logs a CloudWatch.
### Contenedor postgres:
#### - Nombre: postgres
#### - Imagen: postgres:14-alpine
#### - Memoria limit: 512 MB
#### - Puerto: 5432
#### - Variables de entorno: Añade las variables necesarias para la configuración de PostgreSQL.
#### - Logging: Configura el logging para enviar los logs a CloudWatch.
#### - Orden de despliegue: Primero el contenedor de la base de datos (postgres) y luego el contenedor del microservicio (ms-grades).
#### Una vez definidas las tareas, crea un servicio para cada tarea en el clúster.
#### - Selecciona Fargate como tipo de lanzamiento.
#### - Configura el número de tareas deseadas (por ejemplo, 1).
#### - Service name: por ejemplo el nombre del contenedor de nuestro docker-compose.yml
#### - Configura un Application Load Balancer (ALB) para distribuir el tráfico entre las tareas.
#### - Crea un target group para cada servicio y configura las reglas de escucha en el ALB.
#### - Configura la red y la seguridad para el servicio.
#### - Selecciona la VPC y las subredes adecuadas.
#### - Configura el grupo de seguridad para permitir el tráfico entrante en los puertos necesarios (8001, 8002, 5532, 3307).
#### Revisa y crea el servicio.
#### - Cluster VPC: selecciona la VPC donde se desplegarán los servicios.
#### - Subnets: selecciona las subredes públicas o privadas según tu arquitectura.
#### - auto-scaling: configura las políticas de autoescalado si es necesario. en este caso no es necesario.
#### Para que no de error en los ms-grades y ms-users al no encontrar las bases de datos, debemos esperar a que los contenedores de las bases de datos estén en estado "RUNNING" antes de iniciar los contenedores de los microservicios.
#### - Añadir un sleep en el dockerfile no es la mejor práctica, pero puede servir como solución temporal.
#### - También se puede añadir en el application.properties de cada microservicio una configuración para darle margen de tiempo para que la base de datos esté disponible antes de intentar conectarse.
#### Habría que añadir las siguientes propiedades:
```properties
 spring.datasource.hikari.connection-timeout=20000
 spring.datasource.hikari.maximum-pool-size=15
 spring.datasource.hikari.minimum-idle=0
 spring.datasource.hikari.initialization-fail-timeout=-1
 spring.datasource.continue-on-error=true
```
#### Finalmente, una vez que los servicios estén en ejecución, puedes acceder a los microservicios a través del DNS público del Application Load Balancer.

# Kubernetes
#### Ahora vamos a desplegar nuestros servicios dockerizados en un clúster de Kubernetes.
#### Primero se instala Kube y Minikube en tu máquina local.
#### Para instalar Kubectl, sigue las instrucciones oficiales en: https://kubernetes.io/docs/tasks/tools/install-kubectl/
#### Para instalar Minikube, sigue las instrucciones oficiales en: https://minikube.sigs.k8s.io/docs/start/
#### Luego, inicia Minikube para crear un clúster local de Kubernetes.
```bash
 minikube start --driver=docker
```
#### Para parar el clúster de Minikube, usa el comando:
```bash
 minikube stop
```
#### para arrancar de nuevo simplemente usa:
```bash
 minikube start
```
#### Para ver el estado del clúster, usa el comando:
```bash
 minikube status
```
#### Creamos un deployment para cada servicio y base de datos.
#### Comenzaremos con mysql
```kubectl create deployment mysql8 --image=mysql:8 --port=3306 --dry-run=client -o yaml > deployment-mysql.yaml```
#### Adaptamos el archivo deployment-mysql.yaml para añadir las variables de entorno necesarias y un volumen persistente.
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql8
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql8
  template:
    metadata:
      labels:
        app: mysql8
    spec:
      containers:
      - image: mysql:8
        name: mysql
        ports:
        - containerPort: 3306
        env:
          - name: MYSQL_ROOT_PASSWORD
            value: admin
          - name: MYSQL_DATABASE
            value: ms-users
```
#### creamos el deployment de mysql en base a el archivo modificado:
```bash
 kubectl apply -f deployment-mysql.yaml
```
#### comprobamos que se haya levantado:
```bash
 kubectl get deployments
 kubectl get pods
```
#### Exponemos el puerto del servicio mysql:
```bash
 kubectl expose deployment mysql8 --port=3306 --type=ClusterIP
```
#### comandos utiles:
```bash
 kubectl get services
 kubectl describe service mysql8
 kubectl logs <pod_name>
 kubectl exec -it <pod_name> -- /bin/bash
```
#### Creamos el deployment para ms-users con el siguiente comando:
```bash
kubectl create deployment ms-users --image=spgomezinetum/users:latest --port=8001
```

#### exponemos el puerto del servicio ms-users:
```bash
 kubectl expose deployment ms-users --port=8001 --type=LoadBalancer
```

#### Para ver la url del servicio ms-users, usa el comando:
```bash
 minikube service ms-users --url
 #Si no funciona probar esto:
 kubectl port-forward pod/ms-users-5fc58b5877-4fhzb 8080:8001 (revisar)
 kubectl port-forward deployment/ms-users 8001:8001 
```

#### Para borrar los servicios y deployments, usa los siguientes comandos:
```bash
 kubectl delete deployment ms-users
 kubectl delete service ms-users
```

#### Si quisieramos actualizar la imagen del servicio ms-users, primero construimos la nueva imagen y la subimos a Docker Hub, luego actualizamos el deployment con el siguiente comando:
```bash
 kubectl set image deployment ms-users users=spgomezinetum/users:v2
```

#### Si queremos añadir un pod adicional para ms-users, simplemente aumentamos el número de replicas en el deployment:
```bash
 kubectl scale deployment ms-users --replicas=3
```
#### Configuración de los servicios en yaml para mysql ejecutando el siguiente comando:
```bash
 kubectl get service mysql8 -o yaml > svc-mysql.yaml
```
####  Para el deployment de ms-users ejecutamos el siguiente comando:
```bash
 kubectl create deployment ms-users --port=8001 --image=spgomezinetum/users:latest --dry-run=client -o yaml > deployment-users.yaml
```
#### Adaptamos el archivo deployment-users.yaml
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ms-users
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ms-users
  template:
    metadata:
      labels:
        app: ms-users
    spec:
      containers:
      - image: spgomezinetum/users:latest
        name: users
        ports:
        - containerPort: 8001
```

#### Creamos el deployment y el service de Grades y postgres de la misma manera que hicimos con ms-users y mysql, adaptando los nombres, puertos, imágenes y variables de entorno necesarias para cada servicio y base de datos.
#### Una vez tengamos los .yaml se aplican con el siguiente comando:
```bash
 kubectl apply -f deployment-users.yaml
 kubectl apply -f svc-users.yaml
```

#### Se obtiene la url del servicio ms-users con el siguiente comando:
```bash
 minikube service ms-users --url
```

#### Para eliminar deployments o services, simplemente usamos el comando:
```bash
 kubectl delete -f deployment-users.yaml
 kubectl delete -f svc-users.yaml
```

#### Vamos a crear persistent volumes para las bases de datos mysql y postgres. Pueden revisarse en los archivos pv-mysql.yaml y pv-postgres.yaml respectivamente.
#### Para crear un persistent volume, ejecutamos el siguiente comando:
```bash
 kubectl apply -f mysql-pv.yaml
 kubectl apply -f postgres-pv.yaml
```
#### Luego, creamos los persistent volume claims para cada base de datos. Pueden revisarse en los archivos pvc-mysql.yaml y pvc-postgres.yaml respectivamente.
```bash
 kubectl apply -f mysql-pvc.yaml
 kubectl apply -f postgres-pvc.yaml
```
#### Finalmente, adaptamos los deployments de mysql y postgres para montar los volúmenes persistentes en los contenedores. Se puede revisar en los archivos deployment-mysql.yaml y deployment-postgres.yaml respectivamente.
#### Tambien podemos crear configmaps y secrets para almacenar la configuración y las credenciales de las bases de datos de manera segura. Pueden revisarse en los archivos configmap-db.yaml y secret-db.yaml respectivamente.
#### Para crear un configmap, ejecutamos el siguiente comando:
```bash
 kubectl apply -f configmap.yaml
```
#### Para crear un secret, ejecutamos el siguiente comando:
```bash
 kubectl apply -f secret.yaml
```

## Spring Cloud para Kubernetes
#### Spring Cloud Kubernetes es un proyecto que facilita la integración de aplicaciones Spring Boot con Kubernetes, proporcionando características como descubrimiento de servicios, configuración centralizada y gestión de secretos.
#### Para utilizar Spring Cloud Kubernetes en tus aplicaciones, primero debes agregar la dependencia correspondiente en los archivos pom.xml de tus microservicios.
#### Habilitamos la anotacion @EnableDiscoveryClient en la clase principal de cada microservicio para permitir el descubrimiento de servicios en Kubernetes.
#### Eliminamos del client la referencia a la url en la anotación FeignClient, ya que ahora se resolverá automáticamente a través del descubrimiento de servicios de Kubernetes.
#### reconstruimos las imágenes de los microservicios y las subimos a Docker Hub.
#### añadimos estas dos propiedades a cada microservicio para configurar el descubrimiento de servicios en Kubernetes:
```properties
 spring.cloud.kubernetes.secrets.enable-api=true
 spring.cloud.kubernetes.discovery.all-namespaces=true
```
#### Creamos un clusterrolebinding para dar permisos de administrador a la cuenta de servicio por defecto en el namespace default, lo cual es necesario para que Spring Cloud Kubernetes pueda acceder a los recursos de Kubernetes y realizar el descubrimiento de servicios correctamente.
```bash
kubectl create clusterrolebinding admin --clusterrole=cluster-adm
in --serviceaccount=default:default
```
#### Finalmente, aplicamos los cambios en el clúster de Kubernetes y verificamos que los microservicios puedan descubrirse entre sí utilizando el descubrimiento de servicios de Kubernetes.

