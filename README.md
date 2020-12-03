# OpenAPI Apigen Extension

<p align="center">
	<a href="https://apiaddicts.org/">
	  <img src="https://www.apiaddicts.org/logo.png">
	</a>
</p>

# contributors
## CloudAPPi
CloudAppi is one leader in APIs in global word. See the [CloudAPPi Services](https://cloudappi.net) 

## Madrid Digital
Madrid Digital is a public administration in Spain. See the [Comunidad de Madrid website](https://www.comunidad.madrid/)


## Proyecto

### Esquema
    x-apigen-project:
      name: string
      description: string
      version: string
      java-properties:
        group-id: string
        artifact-id: string

### Definición
 - `x-apigen-project`: Apartado donde se define la información sobre el proyecto
   - `name`: Nombre del proyecto
     - `description`: Breve descripción del proyecto
     - `version`: Version en la que se encuentra el proyecto
     - `java-properties`: Apartado donde se define la información específica de java del proyecto
       - `group-id`: Nombre del paquete inicial donde estará el proyecto, en caso de ser varias palabras, estarán separadas por `.`
       - `artifact-id`: Nombre que identificará el proyecto
    
### Ejemplo
    x-apigen-project:
      name: Colores
      description: Este sería el proyecto de los colores
      version: 1.0.0
      java-properties:
        group-id: the.group
        artifact-id: app
        
## Modelos

### Esquema
    x-apigen-models:
      <model_name>:
        relational-persistence:
          table: string
        attributes:
          - name: string
            type: string [ENUM[Array, String, Boolean, Double, Float, Integer, Long, LocalDate y OffsetDateTime, ComposedID] o <model_name>]
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
                  
### Definición
- `x-apigen-models`: Apartado donde se definen los modelos del proyecto
  - `<model_name>`: Nombre del modelo
    - `relational-persistence`: Apartado en el que se indican todos los aspectos relacionados con la peristencia relacional del modelo
      - `table`: Nombre de la tabla en la base de datos
    - `attributes`: Apartado que contiene los atributos del modelo
      - `name`: Nombre del atributo
      - `type`: Tipo del atributo, los tipos soportados son: [Array, String, Boolean, Double, Float, Integer, Long, LocalDate, OffsetDateTime, ComposedID] o en caso de ser de otro modelo, el nombre de este
      - `items-type`: En el caso que el valor de `type` sea un `Array`, este campo se debe definir con el nombre del modelo al que hace referencia
      - `attributes`: Apartado similar a `attributes` del modelo, solo se utiliza cuando el type es ComposedID para indicar los atributos que forman parte de dicho identificador
      - `relational-persistence`: Apartado en el que se indican todos los aspector relacionados con la persistencia relacional del atributo del modelo
        - `column`: Nombre de la columna en la base de datos
        - `columns`: Relacion de las columnas de la tabla actual a otra tabla relacionada cuando se trata de un modelo con un identificador compuesto (cada entrada se define como clave : valor donde la clave es el nombre en la tabla actual y el valor es el nombre en la tabla relacionada)
        - `primary-key`: Indica si es la clave primaria, en caso de no indicarse el valor por defecto será `false`
        - `foreign-column`: Indica el nombre de la columna en la tabla relacionada
        - `foreign-columns`: Relacion de las columnas  en la tabla relacionada respecto a la actual cuando se trata de un modelo con un identificador compuesto (cada entrada se define como clave : valor donde la clave es el nombre en la tabla relacionada y el valor es el nombre en la tabla actual)
        - `intermediate-table`: Nombre de la tabla intermedia en aquellos atributos que representan una relación muchos a muchos
        - `owner`: Indica si esta parte de la relación es el propietario de esta, necesario en las relaciones uno a uno o uno a muchos
        - `sequence`: Apartado donde se puede indicar de forma opciónal el generador ha utilizar para la clave primaria
      - `validations`: Apartado en el que se definen las validaciones del atributo
        - `type`: Los tipos soportados para la validación son los siguiente [NotNull, Size, Min, Min, Email, NotEmpty, NotBlank, Positive, PositiveOrZero, Negative, NegativeOrZero, Past, PastOrPresent, Future, FutureOrPresent, Pattern, Digits, DecimalMin, DecimalMax]
        - `min`: Valor utilizado cuando el valor del campo `type` es `Size`
        - `max`: Valor utilizado cuando el valor del campo `type` es `Size`
        - `regex`: Valor utilizado cuando el valor del campo `type` es `Pattern`
        - `value`: Valor utilizado cuando el valor del campo `type` es `Min`, `Max`, `DecimalMin` o `DecimalMax` 
        - `integer`: Valor utilizado cuando el valor del campo `type` es `Digits`, representra cuantos números como máximo puede tener la parte entera del número
        - `fraction`: Valor utilizado cuando el valor del campo `type` es `Digits`, representra cuantos números como máximo puede tener la parte decimal del número
        - `inclusive`: Valor utilizado cuando el valor del campo `type` es `DecimalMin` o `DecimalMax` 

### Ejemplo
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

## Ampliación de los path

### Esquema
    paths:
      <path>:
        ...
        x-apigen-binding:
          model: string
          
### Definición
 - `x-apigen-binding`: Apartado en el que se define la unión entre el endpoint y el modelo que usaremos
   - `model`: En este campo podremos el nombre del modelo
 
### Ejemplo
    paths:
      /colors:
        x-apigen-binding:
          model: Color
          
## Ampliación del apartado schema del requestBody

### Esquema

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

### Definición

- `x-apigen-mapping`: Apartado en el que se definen todos los datos de mapeo entre los recursos y los modelos. Cada atributo de un recurso de entrada, incluyendo el propio recurso de entrada pueden tener definido este apartado, si no esta definido se asume que tiene los valores por defecto indicados en cada apartado.
  - `model`: Nombre del modelo al que representa el recurso o atributo, si a nivel de recurso no se indica no se considera un recurso estandar de entrada
  - `field`: Nombre del atributo del modelo al que se mapeará el atributo del recurso, por defecto si no se indica nada se mapeará a un atributo con el mismo nombre si existe, sino se ingnorará. Existen dos casos específicos en los que es obligatorio definir este campo:
    - Cuando el nombre del atributo en el recurso y en el modelo son diferentes
      ````
        nombre:
          type: string
          x-apigen-mapping:
            field: primerNombre
      ````
    - Cuando el atributo en el recurso es una abreviatura del identificador de un modelo anidado
      ````
        color:
          type: string
          x-apigen-mapping:
            model: color
            field: valorCromatico.id
      ````

## Ampliación del apartado schema de los responses

### Precondiciones

La extensión de Apigen para OpenAPI nos obliga a tener en cuenta una serie de condiciones para que las respuestas de los endpoints cumplan con el estandar.

#### Tener definida una respuesta estandar

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

#### Tener definida una única respuesta estandar de colección por cada modelo

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

#### Tener definida una única respuesta estandar simple por cada modelo

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

### Esquema

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
            
### Definición
- `x-apigen-mapping`: Apartado en el que se definen todos los datos de mapeo entre los recursos y los modelos. Cada recurso de salida debe tener definido este apartado
  - `model`: Nombre del modelo al que representa el recurso
  - `field`: Nombre del atributo del modelo al que se representará el atributo del recurso

### Ejemplo

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
