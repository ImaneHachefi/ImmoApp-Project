### **ImmoApp — Brique Immobilière**



#### **Structure**

\- `ImmoApp/` → Backend Spring Boot (API REST)

\- `immoapp\_ia/` → Microservice IA Python FastAPI



#### **Lancer le projet**



##### **Backend Spring Boot**

cd ImmoApp

mvn spring-boot:run



##### **Microservice IA**

cd immoapp\_ia

.venv\\Scripts\\activate

uvicorn main:app --reload --port 8000

