# GitHub Copilot Instructions

## Code Style

- Use Kotlin for new classes
- Follow Kotlin naming conventions (camelCase for functions/properties, PascalCase for classes)
- Use data classes for DTOs and always annotate them with @JsonRecord
- Prefer immutability (val over var)
- Use nullable types explicitly when needed

## Spring Boot Conventions

- Use `@Configuration` classes for Spring beans
- Inject dependencies via constructor injection
- Use `@Value` for external configuration
- Follow existing patterns in `AorGrunnlagConfig.kt`

## Jackson/ObjectMapper Setup

- Configure ObjectMapper with JavaTimeModule, Jdk8Module, JodaModule
- Disable WRITE_DATES_AS_TIMESTAMPS
- Use date format: yyyy-MM-dd
- Set JsonInclude.Include.NON_EMPTY for serialization

## Testing

- Use JUnit 5 for tests
- Use function names with backticks for readability
- Follow AAA pattern (Arrange, Act, Assert)
- Use meaningful test method names

## Gradle

- Use Kotlin DSL for build files
- Keep dependencies organized by category

## Documentation

- Add KDoc for public APIs
- Explain complex business logic in Norwegian
- Use clear variable names that reduce need for comments

## General Best Practices

- Follow the patterns you see in existing code to the extent that it makes sense, but never suggest new code in java.
- Minimize the use of comments. If you insist on adding a comment, write it in Norwegian.
- Be concise and avoid unnecessary verbosity, unless asked for detailed explanations.
- Do not create code examples unless specifically requested.
- Don't make documentation files unprompted.
