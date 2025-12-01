
# 🛠️ Apigen ![Release](https://img.shields.io/badge/release-2.0.0-purple) ![Swagger](https://img.shields.io/badge/-openapi-%23Clojure?style=flat&logo=swagger&logoColor=white) ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=flat&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=flat&logo=spring&logoColor=white) [![License: LGPL v3](https://img.shields.io/badge/license-LGPL_v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)

Welcome to **apigen.springboot**, made in spain ![Spain](https://raw.githubusercontent.com/stevenrskelton/flag-icon/master/png/16/country-4x3/es.png "Spain"), the opensource project in Java that allows you to generate an archetype of the springboot framework using the openapi file as a mapping tool between the openapi definition and the database. Click maven to see available mvnrepository dependencies.

<a title="mvnrepository" href="https://mvnrepository.com/artifact/org.apiaddicts.apitools.apigen/apigen">
    <img width="150"  src="https://blog.irontec.com/wp-content/uploads/2019/12/1280px-Maven_logo.svg_-300x76.png">
</a>

### This repository is intended for :octocat: **community** use, it can be modified and adapted without commercial use. If you need a version, support or help for your **enterprise** or project, please contact us 📧 devrel@apiaddicts.org

[![Twitter](https://img.shields.io/badge/Twitter-%23000000.svg?style=for-the-badge&logo=x&logoColor=white)](https://twitter.com/APIAddicts)
[![Discord](https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/ZdbGqMBYy8)
[![LinkedIn](https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/company/apiaddicts/)
[![Facebook](https://img.shields.io/badge/Facebook-%231877F2.svg?style=for-the-badge&logo=Facebook&logoColor=white)](https://www.facebook.com/apiaddicts)
[![YouTube](https://img.shields.io/badge/YouTube-%23FF0000.svg?style=for-the-badge&logo=YouTube&logoColor=white)](https://www.youtube.com/@APIAddictslmaoo)

# 🙌 Join the **Apigen** Adopters list
📢 If Apigen is part of your organization's toolkit, we kindly encourage you to include your company's name in our Adopters list. 🙏 This not only significantly boosts the project's visibility and reputation but also represents a small yet impactful way to give back to the project.

| Organization  | Description of Use / Referenc |
|---|---|
|  [CloudAppi](https://cloudappi.net/)  | Apification and generation of microservices |
| [Acciona](https://www.acciona.com/)  | Generation of microservices |
| [Madrid Digital](https://www.comunidad.madrid/servicios/sede-electronica/madrid-digital/)  | Generation of microservices  |
| [Apiquality](https://apiquality.io/)  | Generation of microservices  |

# 👩🏽‍💻  Contribute to ApiAddicts

We're an inclusive and open community, welcoming you to join our effort to enhance ApiAddicts, and we're excited to prioritize tasks based on community input, inviting you to review and collaborate through our GitHub issue tracker.

Feel free to drop by and greet us on our GitHub discussion or Discord chat. You can also show your support by giving us some GitHub stars ⭐️, or by following us on Twitter, LinkedIn, and subscribing to our YouTube channel! 🚀

[!["Buy Me A Coffee"](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.buymeacoffee.com/apiaddicts)


# 📑 Getting started

## 🖥️ CLI

```bash
java -jar generator-cli.jar generate -f openapi.yaml -o ./code

java -jar generator-cli.jar help
```

## 🐋 Docker compose

```yaml
version: "3.3"
services:
  apigen:
    image: "apiaddicts/apitools-apigen:2.0.0"
    ports:
      - "8080:8080"
```

```bash
curl -X POST "http://localhost:8080/generator/file" \
  -F "file=@openapi.yaml"
```

## Project

### Scheme
    x-apigen-project:
      name: string
      description: string
      version: string
      java-properties:
        group-id: string
        artifact-id: string

### Definition
- `x-apigen-project`: Section where project information is defined
  - `name`: Project name
  - `description`: Brief project description
  - `version`: Project version
  - `java-properties`: Section where specific Java project information is defined
    - `group-id`: Initial package name where the project resides. If it consists of multiple words, they will be separated by `.`
    - `artifact-id`: Identifier name for the project
    - `base-package`: Name of the base package to use in the generated code; if not specified, it is obtained from the `group-id` and `artifact-id`
  - `standard-response-operations`: Section where standard apigen response transformation operations are defined (optional)
    - `<jsonpatch operation node>`: Node that complies with the json-patch standard, allowing optional operations to be declared on all elements of an array

### Samples
    x-apigen-project:
      name: Colors
      description: This would be the Colors project
      version: 1.0.0
      java-properties:
        group-id: the.group
        artifact-id: app

## Models

### Scheme
    x-apigen-models:
      <model_name>:
        relational-persistence:
          table: string
        attributes:
          - name: string
            type: string [ENUM[Array, String, Boolean, Double, Float, BigDecimal, Integer, Long, BigInteger, LocalDate, LocalDateTime, ZonedDateTime, OffsetDateTime, Instant, ComposedID] o <model_name>]
            items-type: string [<model_name>]
            relational-persistence:
              column: string
              primary-key: boolean
              foreign-column: string
              intermediate-table: string
              owner: boolean
            validations:
              - type: string [ENUM[NotNull, Size, Min, Max, Email, NotEmpty, NotBlank, Positive, PositiveOrZero, Negative, NegativeOrZero, Past, PastOrPresent, Future, FutureOrPresent, Pattern, Digits, DecimalMin, DecimalMax]]
                min: integer
                max: integer
                regex: string
                value: integer | string
                integer: integer
                fraction: integer
                inclusive: boolean   

### Definition
- `x-apigen-models`: Section where project models are defined
  - `<model_name>`: Name of the model
    - `relational-persistence`: Section where all aspects related to the model's relational persistence are indicated
      - `table`: Name of the table in the database
    - `attributes`: Section that contains the model's attributes
      - `name`: Name of the attribute
      - `type`: Type of the attribute, supported types are: [Array, String, Boolean, Double, Float, Integer, Long, LocalDate, OffsetDateTime, ComposedID], or in the case of another model, the name of that model
      - `items-type`: In the case of the `type` being an `Array`, this field should be defined with the name of the referenced model
      - `attributes`: Similar to the `attributes` of the model, only used when the `type` is ComposedID to indicate the attributes that make up the identifier
      - `relational-persistence`: Section where all aspects related to the relational persistence of the model's attribute are indicated
        - `column`: Name of the column in the database
        - `columns`: Relationship of columns from the current table to another related table when dealing with a model with a composite identifier (each entry is defined as key: value where the key is the name in the current table and the value is the name in the related table)
        - `primary-key`: Indicates if it is the primary key, if not specified, the default value will be `false`
        - `foreign-column`: Indicates the name of the column in the related table
        - `foreign-columns`: Relationship of columns in the related table to the current table when dealing with a model with a composite identifier (each entry is defined as key: value where the key is the name in the related table and the value is the name in the current table)
        - `intermediate-table`: Name of the intermediate table in those attributes that represent a many-to-many relationship
        - `owner`: Indicates if this part of the relationship is the owner, necessary in one-to-one or one-to-many relationships
        - `sequence`: Section where the optional generator to use for the primary key can be indicated
      - `validations`: Section where attribute validations are defined
        - `type`: The supported types for validation are as follows: [NotNull, Size, Min, Max, Email, NotEmpty, NotBlank, Positive, PositiveOrZero, Negative, NegativeOrZero, Past, PastOrPresent, Future, FutureOrPresent, Pattern, Digits, DecimalMin, DecimalMax]
        - `min`: Value used when the `type` field is `Size`
        - `max`: Value used when the `type` field is `Size`
        - `regex`: Value used when the `type` field is `Pattern`
        - `value`: Value used when the `type` field is `Min`, `Max`, `DecimalMin`, or `DecimalMax`
        - `integer`: Value used when the `type` field is `Digits`, representing the maximum number of digits in the integer part of the number
        - `fraction`: Value used when the `type` field is `Digits`, representing the maximum number of digits in the decimal part of the number
        - `inclusive`: Value used when the `type` field is `DecimalMin` or `DecimalMax`

### Samples
    x-apigen-models:
      Color:
        relational-persistence:
          table: colors
        attributes:
          - name: id
            type: Long
            relational-persistence:
              column: id
              primary-key: true
            validations:
              - type: NotNull
          - name: name
            type: String
            relational-persistence:
              column: name
            validations:
              - type: Pattern
                regex: [A-Z]+
      Composed:
        relational-persistence:
          table: composed
        attributres:
          - name: id
            type: ComposedID
            validations:
               - type: NotNull
            attributes:
              - name: one
                type: String
                relational-persistence:
                  column: c_one
                validations:
                 - type: NotNull
              - name: two
                type: Integer      
                relational-persistence:
                  column: c_two      
                validations:
                 - type: NotNull

## Path expansion

### Scheme
    paths:
      <path>:
        ...
        x-apigen-binding:
          model: string

### Definition
- `x-apigen-binding`: Section where the connection between the endpoint and the model we will use is defined
  - `model`: In this field, we can specify the name of the model.
  - `child-model`: In this field, we will put the name of the child model. Only necessary for self-managed endpoints of parent-child type entities.
  - `child-parent-relation-property`: In this field, we will put the name of the property in the child that relates to the parent. Only necessary for self-managed endpoints of parent-child type entities.

### Sample
    paths:
      /colors:
        x-apigen-binding:
          model: Color

## Extension of the schema section of the requestBody

### Scheme

    schemas:
      <schema>:
        x-apigen-mapping:
          model: string
      type: object
      properties:
        <prop_name>:
          type: string
          x-apigen-mapping:
            model: string
            field: string
        <prop_name_2>:
          x-apigen-mapping:
            model: string

### Definition

- `x-apigen-mapping`: Section where all mapping data between resources and models are defined. Each attribute of an input resource, including the input resource itself, can have this section defined. If it is not defined, it is assumed to have the default values indicated in each section.
  - `model`: Name of the model that the resource or attribute represents. If not specified at the resource level, it is not considered a standard input resource.
  - `field`: Name of the model attribute to which the resource attribute will be mapped. By default, if nothing is specified, it will be mapped to an attribute with the same name if it exists; otherwise, it will be ignored. There are two specific cases in which it is mandatory to define this field:
    - When the name of the attribute in the resource and in the model are different:
      ```yaml
      nombre:
        type: string
        x-apigen-mapping:
          field: primerNombre
      ```
    - When the attribute in the resource is an abbreviation of the identifier of a nested model:
      ```yaml
      color:
        type: string
        x-apigen-mapping:
          model: color
          field: valorCromatico.id
      ```

## Expansion of the response outline section

### Preconditions

The Apigen extension for OpenAPI forces us to take into account a series of conditions so that endpoint responses comply with the standard.

#### Have a defined standard response

````yaml
  schemas:
    standard_response_result:
      properties:
        result:
          type: object
          properties:
            status:
              type: boolean
            http_code:
              type: integer
            errors:
              type: array
              items:
                $ref: '#/components/schemas/standard_error'
            info:
              type: string
            trace_id:
              type: string
            num_elements:
              type: integer
          required:
            - status
            - http_code
            - trace_id
    standard_error:
      type: object
      properties:
        code:
          type: integer
        message:
          type: string
````

#### Have a single standard collection response defined for each model

````yaml
  schemas:
    standard_response_collection_<model>:
      allOf:
        - $ref: "#/components/schemas/standard_response_result"
        - type: object
          properties:
            data:
              type: object
              properties:
                <model_plural>:
                  type: array
                  items:
                    $ref: "#/components/schemas/<model_resource>"
````

#### Have a single simple standard response defined for each model

````yaml
  schemas:
    standard_response_<model>:
      allOf:
        - $ref: "#/components/schemas/standard_response_result"
        - type: object
          properties:
            data:
              $ref: "#/components/schemas/<model_resource>"
````

### Scheme

    schemas:
      <schema>:
        x-apigen-mapping:
          model: string
      type: object
      properties:
        <prop_name>:
          type: string
          x-apigen-mapping:
            field: string

### Definition
- `x-apigen-mapping`: Section where all mapping data between resources and models are defined. Each output resource must have this section defined.
  - `model`: Name of the model that the resource represents.
  - `field`: Name of the model attribute to which the resource attribute will be mapped.

### Sample

    color:
      x-apigen-mapping:
        model: Color
      type: object
      properties:
        json_id:
          type: integer
          x-apigen-mapping:
            field: id
        name:
          type: string
        forms:
          $ref: "#/components/schemas/forms"


## 💛 Sponsors
<p align="center">
	<a href="https://apiaddicts.org/">
    	<img src="https://apiaddicts.cloudappi.net/web/image/4248/LOGOCloudappi2020Versiones-01.png" alt="cloudappi" width="150"/>
        <img src="https://www.comunidad.madrid/sites/default/files/styles/block_teaser_image/public/img/logos-simbolos/logo_centrado_md.png?itok=4rTUhmcj" alt="md" width="150"/>
        <img src="https://apiquality.io/wp-content/uploads/2022/09/cropped-logo-apiquality-principal-1-170x70.png" height = "75">
        <img src="https://apiaddicts-web.s3.eu-west-1.amazonaws.com/wp-content/uploads/2022/03/17155736/cropped-APIAddicts-logotipo_rojo.png" height = "75">
	</a>
</p>