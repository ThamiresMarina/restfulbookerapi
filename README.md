# 🧪 Restful Booker API Tests

Projeto de testes automatizados da API [Restful Booker](https://restful-booker.herokuapp.com/) utilizando **Java, Maven, JUnit 5 e RestAssured**.

---

## 📌 Objetivo
Este projeto foi criado para praticar testes de API (CRUD) com:
- Geração de token de autenticação
- Criação de reservas
- Atualização de reservas
- (Futuro) Deleção de reservas

Todos os payloads de requisição estão em arquivos **JSON externos**, facilitando a manutenção e evolução dos testes.

---

## 📂 Estrutura do Projeto

RESTFULBOOKER/
├── src/
│ ├── main/
│ │ ├── java/ # (vazio por enquanto)
│ │ └── resources/
│ └── test/
│ ├── java/
│ │ └── BookingApiTest.java # Classe de testes
│ └── resources/
│ ├── Auth.json # Dados de autenticação
│ ├── CriarBooking.json # Payload para criar reserva
│ └── AtualizarBooking.json # Payload para atualizar reserva
└── pom.xml


---

## ⚙️ Tecnologias
- **Java 17+**
- **Maven**
- **JUnit 5**
- **RestAssured**

---

## ▶️ Como executar
1. Clone este repositório:
   ```bash
   git clone https://github.com/ThamiresMarina/restfulbookerapi.git

2. Entre na pasta:

cd restfulbooker-tests


3. Rode os testes com Maven:

mvn test


4. Para rodar apenas um teste específico:

mvn -Dtest=BookingApiTest#testGerarToken test

## Testes Implementados

testGerarToken → Gera um token de autenticação

testCriarBooking → Cria uma reserva

testAtualizarBooking → Atualiza uma reserva com token de autenticação

(em breve) testDeletarBooking → Deletar uma reserva existente

## Futuras melhorias

Adicionar o fluxo completo CRUD

Implementar Data Driven Tests

Adicionar pipeline CI/CD (GitHub Actions)   
