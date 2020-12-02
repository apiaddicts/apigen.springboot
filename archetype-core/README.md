# Procedimientos

## Sobreescribir el mensaje y/o código de un error

Los errores actualmente gestionados de forma automática se pueden encontrar en la enum `DefaultApigenError`.

Si queremos sobreescribir el mensaje de un error en concreto debemos definirlo en el archivo `.properties` de nuestra aplicación de la siguiente forma

    apigen.errors.<ERROR_NAME>.code
    apigen.errors.<ERROR_NAME>.message-template

Donde `<ERROR_NAME>` es el valor de la enum que queremos sobreescribir.
En caso de que queramos interpolar los valores en el mensaje se deberán indicar con `{n}` donde `n` es la posición del valor a interpolar en base cero, podemos saber cuantos parámetros tiene viendo el mensaje por defecto. Hay que tener en cuenta que este template se gestiona mediante `MessageFormat.format` por lo que acepta cualquier expresión que este método soporte. 

Ejemplo:

    apigen.errors.PATH_NOT_IMPLEMENTED.code=9191
    apigen.errors.PATH_NOT_IMPLEMENTED.message-template=Not implemented ({0})
    
## Definir un nuevo error

Su definición en el `.properties` es exactamente igual que cuando sobreescribimos uno estándar.

A la hora de implementar errores estándar se recomienda definirlos en una o varias clases / enums así como excepciones específicas.

Ejemplo:

    enum BussinessErrors {
        WRONG_INVOICE
    }
    
    public class WrongInvoiceException extends CustomApigenException {
    	public WrongInvoiceException(String invoiceID) {
    		super(BusinessErrors.WRONG_INVOICE.name(), invoiceID);
    		setHttpStatus(HttpStatus.I_AM_A_TEAPOT);
    	}
    }
    
## Definir un nuevo recurso de visualización

Actualmente solo se genera un recurso de visualización por entidad, esto permite simplificar y unificar nuestros recursos, pero en ocasiones puede que necesitemos definir recursos de visualización especiales.

Para definir un nuevo recurso de visualización necesitamos definir la clase o clases que van a representar estos recursos y anotarlas todas con `@ApigenEntityOutResource`, esta anotación permitirá que de forma automática nuestro `ResourceNamingTranslator` traduzca los nombres de `json` a `java`

Ejemplo:

    @ApigenEntityOutResource
    class CustomUserResource {
        @JsonProperty("email")
        private String username;
        @JsonProperty("scopes")
        private List<CustomRoleResource> roles;
    }
    
    @ApigenEntityOutResource
    class CustomRoleResource {
        @JsonProperty("value")
        private String name;
    }

Estos nuevos recursos podemos rellenarlos de forma manual, definiendo un nuevo método en el mapper correspondiente o combinando ambas estrategias.

## Modificar el comportamiento a la hora de crear un recurso

Cuando creamos un recurso se realiza el siguiente flujo:

1 - Los datos del recurso `Create<Model>Resource` se transforman en una entidad `<Model>`

2 - Las entidades relacionadas que se hayan indicado se crean o recuperan y se asocian a la entidad. Esta lógica se realiza dentro del `<Model>RelationManager` correspondiente.

3 - Se crea la entidad en sí

Si deseamos ejecutar código adicional durante este flujo recomendamos usar los métodos adicionales descritos en el apartado [Ampliar los servicios](#ampliar-la-funcionalidad-de-los-servicios-definidos)

Si deseamos modificar el comportamiento del `RelationManager` somos libres de hacerlo pero hay que tener en cuenta que los métodos definidos se llaman siempre por lo que si la lógica que queremos implementar es diferente, se recomienda implementar otro método y sobreescribir en el servicio la parte oportuna.
Por defecto el `RelationManager` solo se encarga de gestionar aquellas relaciones de las cuales se es el propietario (owner), son aquellas que no disponen de un `mappedBy`.

## Modificar el comportamiento a la hora de actualizar un recurso

Cuando modificamos un recurso se realiza el siguiente flujo:

1 - Los datos del recurso `Create<Model>Resource` se transforman en una entidad `<Model>`

2 - Se recupera la entidad persistida

3 - Se actualizan los datos básicos (aquellos que no son entidades) de la entidad a la entidad persistida mediante el método `updateBasicDataPartially` del `<Model>Service`

4 - Las entidades relacionadas que se hayan indicado se relacionan a partir del método `updateRelations` del `<Model>RelationManager` correspondiente.

5 - Se guardan los cambios

Si deseamos ejecutar código adicional durante este flujo recomendamos usar los métodos adicionales descritos en el apartado [Ampliar los servicios](#ampliar-la-funcionalidad-de-los-servicios-definidos)

Si deseamos modificar el comportamiento del `RelationManager` somos libres de hacerlo pero hay que tener en cuenta que los métodos definidos se llaman siempre por lo que si la lógica que queremos implementar es diferente, se recomienda implementar otro método y sobreescribir en el servicio la parte oportuna.
Por defecto el `RelationManager` solo se encarga de gestionar aquellas relaciones de las cuales se es el propietario (owner), son aquellas que no disponen de un `mappedBy`.

## Redefinir la gestión de errores

Por defecto la clase `ApigenControllerAdvice` se encarga de la gestión de los errores. Si queremos gestionar un erro actualmente no gestionado o sobreescribir la gestión sobre uno ya definido, debemos de definir otro `@ControllerAdvice` en nuestro proyecto, gestionando dicha excepción en concreto y definiéndolo con un orden de prioridad mayor.

Ejemplo:

    @ControllerAdvice
    @Order(Ordered.HIGHEST_PRECEDENCE)
    class CustomControllerAdvice {
    
    	@ResponseBody
    	@ExceptionHandler(Exception.class) // <-- The exception class
    	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    	public ApiResponse exception(Exception ex) {
    	    // TODO implement logic here
    	}
    
    }

## Redefinir la gestión de los `ApiResponse`

Por defecto la clase `ApiResponseBodyAdvice` se encarga de ampliar los datos de las respuestas (`ApiResponse` y `ResponseEntity<ApiResponse>`)

Si deseamos modificar dicha lógica debemos de extender la clase e implementar nuestra propia lógica.

Ejemplo:

    @ControllerAdvice
    public class CustomBodyAdvice extends ApiResponseBodyAdvice {
    
    	public CustomBodyAdvice(ApigenProperties apigenProperties) {
    		super(apigenProperties);
    	}
    	
    	@Override
    	public ApiResponse beforeBodyWrite(ApiResponse apiResponse, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
    		apiResponse.withResultUpdatedElements(100);
    		return apiResponse;
    	}
    }

## Definir limitaciones en los recursos de búsqueda

Por defecto en todos los recursos de búsqueda está configurada una profundidad de expansión de `1` nivel. (Por ejemplo si nuestro recurso es un usuario y tiene roles podremos hacer un `$expand=roles`)

Podemos configurar el nivel de la profundidad de expansión mediante la siguiente propiedad en el `.properties`

    apigen.api.expand=2

Dado que la configuración anterior nos permite definir un comportamiento genérico en nuestra api, en ocasiones podemos tener la necesidad de no permitir expandir algunas propiedades de un recurso anidado o solo permitir cierto subconjunto de ellas, para ello se pueden definir una serie de condiciones de expansión en el `.properties`

    apigen.api.paths[PATH_EXPRESSION].expand.allowed=value,other_value
    apigen.api.paths[PATH_EXPRESSION].expand.excluded=value,other_value
    apigen.api.paths[PATH_EXPRESSION].expand.level=3

El valor `allowed` indica aquellas propiedades por las que permitimos expandir.
El valor `excluded` indica aquellas propiedades por las que no permitimos expandir.
Se recomienda usar solo `allowed` o `excluded` en un mismo `PATH_EXPRESSION`.

El valor `level` indica el nivel de profundidad permitido (sobreescribe el valor general si lo indicamos, sino se utiliza el valor general), en caso de que indiquemos el valor `allowed` no haría falta definirlo.

En `PAT_EXPRESSION` debemos de indicar un valor fijo o un path regex.

    /explicit/path
    
    /path-by-id/{\:.*}
    Se aplica a valores como /path-by-id/23
    
    /path-by-regex-exclude/{\:^(?!explicit$|other$).*}
    Se aplica a valores como /path-by-regex-exclude/algo pero no a /path-by-regex-exclude/explicit ni /path-by-regex-exclude/other

Aunque se permita definir las propiedades del expand para un a url o un grupo de ellas mediante las properities, esto no está recomendado si podemos utilizar anotaciones.

Para definir los limites de un expand mediante anotaciones debemos de añadir la anotación `@ApigenExpand` en el método que implementa el endpoint en el controlador.

Esta anotación nos permite definir 3 valores:

- `allowed`: Permite definir un conjunto de valores permitidos en el expand. Si este valor se indica no se tendrá en cuenta lo definido en `excluded` ni en `maxLevel`.
- `excluded`: Permite definir un conjunto de valores no permitidos en el expand. A los valores que no estén indicados en el `excluded` se les aplicará la validación de máxima profundidad.
- `maxLevel`: Permite definir el nivel máximo de profundidad de un expand. Si no se indica se utilizará el nivel genérico definido en las porperties y si este tampoco ha sido indicado entonces se utilizará su valor por defecto (`1`).

Ejemplo:

        @ApigenExpand(excluded = {"one", "one.two", "one.two.three"}, maxLevel = 3)
        @GetMapping("/annotation/excluded-and-level")
        public void getAnnotationExcludedAndLevel() {
            ...
        }

Cualquier endpoint que tenga una anotación de `@ApigenExpand` será ignorado por el evaluador de paths del interceptor de expansión.
Por lo tanto si un endpoint que tiene dicha anotación coincide con algún path especificado en las properties este será ignorado y solo se aplicarán las validaciones indicadas mediante la anotación.

## Modificar la cabecera de trazabilidad

Por defecto la cabecera de trazabilidad tiene el nombre `x-trace-id`.

Si deseamos cambiar ese valor debemos definirlo en el archivo `.properties`

    apigen.trace-header=x-custom-name

## Ampliar la funcionalidad de los servicios definidos

Todos los servicios que realizan operaciones de escritura extienden del `AbstractCrudService`, este ofrece una serie de métodos que se pueden sobreescribir para implementar lógicas determinadas, si queremos mayor grado de personalización también podemos sobreescribir los otros métodos que ofrece.

| Método |
| ------ |
| `preCreate(E entity)` |
| `preCreateBeforeManageRelations(E entity)` |
| `preCreateAfterManageRelations(E entity)` |
| `postCreate(E entity)` |
| `preUpdate(E persistedEntity, E entity, Set<String> fields)` |
| `preUpdateBeforeManageBasicData(E persistedEntity, E entity, Set<String> fields)` |
| `preUpdateAfterManageBasicData(E persistedEntity, E entity, Set<String> fields)` |
| `preUpdateBeforeManageRelations(E persistedEntity, E entity, Set<String> fields)` |
| `preUpdateAfterManageRelations(E persistedEntity, E entity, Set<String> fields)` |
| `postUpdate(E persistedEntity, E entity, Set<String> fields)` |
| `preDelete(E entity)` |
| `postDelete(E entity)` |
| `preSave(E entity)` |
| `postSave(E entity)` |

## Ampliar la funcionalidad del `ApigenContext`

Por defecto `ApigenContext` nos permite acceder al valor de la cabecera de trazabilidad de forma segura dentro del contexto de la petición.

Hay ciertas ocasiones en las que nos puede ser de utilidad almacenar más información dentro del contexto de la petición, como por ejemplo otras cabeceras.

Para ello se recomienda extender `ApigenContext` e implementar los interceptores que creamos oportunos.

Ejemplo:

    public class MyAppContext extends ApigenContext {
    	private static final String BUSINESS_ID = "BUSINESS_ID";
    	public static Long getBusinessId() {
    		return (Long) getRequestAttribute(BUSINESS_ID);
    	}
    	public static void setBusinessId(Long bussinesId) {
    		setRequestAttribute(BUSINESS_ID, bussinesId);
    	}
    }
    
## Dialecto de Hibernate no soportado

Apigen actualmente soporta los dialectos de H2, MySQL, Oracle y PostgreSQL.
Si intentamos utilizar un dialecto distinto nos aparecerá el siguiente mensaje de error en el log:

    Dialect DIALECT_CLASS not supported natively by Apigen, consult documentation to extend your own dialect

Actualmente Apigen amplia la definicón de funciones del dialecto para que las funciones avanzadas del filtro (como REGEXP) estén soportadas.
Cuando arraquemos una aplicación que no tenga definida algúna función requerida nos aparecerá el siguente mensaje de error en el log:

    Function FUNCTION_NAME not defined in current dialect DIALECT_CLASS, operation REGEXP not supported, consult documentation to extend your own dialect

En este caso debemos podemos crear en nuestro proyecto una clase por cada dialecto al que queramos dar soporte y configurarlo en el perfil oportuno.

Ejemplo:

**Nota**: Por simplicidad aunque H2 esté soportada, en este ejemplo asumiremos que no lo está.

    Mensajes de error en el LOG: 
    
    Dialect org.hibernate.dialect.H2Dialect not supported natively by Apigen, consult documentation to extend your own dialect
    Function apigen_regexp not defined in current dialect org.hibernate.dialect.H2Dialect, operation REGEXP not supported, consult documentation to extend your own dialect

&#13;

    package my.project.dialects
    
    public class MyH2Dialect extends org.hibernate.dialect.H2Dialect {
    
        public MyH2Dialect() {
            super();
            this.registerFunction("apigen_regexp", new SQLFunctionTemplate(StandardBasicTypes.BOOLEAN, "REGEXP_LIKE(?1,?2)"));
        }
    }
    
&#13;

    application-dev.proeprties
    
    spring.jpa.properties.hibernate.dialect = my.project.dialects.dialects.MyH2Dialect
    
    
# Tabla descriptiva del comportamiento por defecto en la creación

| Tipo de campo     | Dato en el json | Resultado           | Notas                                                                                                                              |
|-------------------|-----------------|---------------------|------------------------------------------------------------------------------------------------------------------------------------|
| BASIC             | NULL            | NULL                |                                                                                                                                    |
| BASIC             | VALUE           | VALUE               |                                                                                                                                    |
| ENTITY            | NULL            | NULL                |                                                                                                                                    |
| ENTITY            | WITH ID         | RETRIEVE AND ASSIGN |                                                                                                                                    |
| ENTITY            | WITHOUT ID      | CREATE AND ASSIGN   | La creación vuelve a aplicar de forma recursiva la lógica de esta tabla                                                            |
| ENTITY COLLECTION | NULL            | NULL                |                                                                                                                                    |
| ENTITY COLLECTION | WITH ID         | RETRIEVE AND ASSIGN | Se aplica a cada elemento de la colección que tenga id                                                                             |
| ENTITY COLLEICTON | WITHOUT ID      | CREATE AND ASSIGN   | Se aplica a cada elemento de la colección que no tenga id, la creación vuelve a aplicar de forma recursiva la lógica de esta tabla |

# Tabla descriptiva del comportamiento por defecto en la actualización

| Tipo de campo     | Dato en el json | Dato persistido | Resultado           | Notas                                                                                         |
|-------------------|-----------------|-----------------|---------------------|-----------------------------------------------------------------------------------------------|
| BASIC             | NULL            | ANY             | NULL                |                                                                                               |
| BASIC             | VALUE           | ANY             | VALUE               |                                                                                               |
| BASIC             | UNDEFINED       | ANY             | ANY                 |                                                                                               |
| ENTITY            | NULL            | NULL            | NULL                |                                                                                               |
| ENTITY            | NULL            | ENTITY          | NULL                | Simplemente se elimina la relación, la subentidad continua existiendo                         |
| ENTITY            | WITH ID         | NULL            | RETRIEVE AND ASSIGN |                                                                                               |
| ENTITY            | WITH ID         | ENTITY          | RETRIEVE AND ASSIGN | La antigua entidad relacionada continua existiendo, simplemente se modifica la relación       |
| ENTITY            | WITHOUT ID      | NULL            | ERROR               |                                                                                               |
| ENTITY            | WITHOUT ID      | ENTITY          | ERROR               |                                                                                               |
| ENTITY            | UNDEFINED       | ANY             | ANY                 |                                                                                               |
| ENTITY COLLECTION | NULL            | NULL            | NULL                |                                                                                               |
| ENTITY COLLECTION | NULL            | ENTITY          | NULL                | Simplemente se eliminan las relaciónes, la subentidades continua existiendo                   |
| ENTITY COLLECTION | WITH ID         | NULL            | RETRIEVE AND ASSIGN |                                                                                               |
| ENTITY COLLECTION | WITH ID         | ENTITY          | RETRIEVE AND ASSIGN | Las antiguas entidades relacionadas continuan existiendo, simplemente se modifica la relación |
| ENTITY COLLECTION | WITHOUT ID      | NULL            | ERROR               |                                                                                               |
| ENTITY COLLECTION | WITHOUT ID      | ENTITY          | ERROR               |                                                                                               |
| ENTITY COLLECTION | UNDEFINED       | ANY             | ANY                 |                                                                                               |

# Fechas y horas

Los dos tipos de Java soportados actualmente de forma automática son `LocalDate` para las fechas y `OffsetDateTime` para las fechas con hora.
Las fechas con hora se recomienda almacenarlas en formato UTC por lo que por defecto el api las devolverá en UTC.

Relación de tipos con bases de datos más comunes:

| Tipo java | Ejemplo input | Ejemplo output | Tipo PostgreSQL | Ejemplo PostgreSQL | Explicación |
| --- | ---- | --- | ---- | --- | ---- |
| LocalDate | 2020-05-05 | 2020-05-05 | DATE | 2020-05-05 | - |
| OffsetDateTime | 2020-05-05T14:00:00+04:00 | 2020-05-05T10:00:00Z | TIMESTAMP | 2020-05-03 10:00:00 | En este caso java utiliza el offset del timezone que tiene configurado para transformar el valor y almacenarlo en base de datos, debido a esto se recomienda configurar la aplicación para usar UTC|
| OffsetDateTime | 2020-05-05T10:00:00Z | 2020-05-05T10:00:00Z | TIMESTAMP | 2020-05-03 10:00:00 | Igual que el caso anterior pero ya usamos UTC en el formato de entrada |


| Tipo java | Ejemplo input | Ejemplo output | Tipo MySql | Ejemplo MySql | Explicación |
| --- | ---- | --- | ---- | --- | ---- |
| LocalDate | 2020-05-05 | 2020-05-05 | DATE | 2020-05-05 | - |
| OffsetDateTime | 2020-05-05T14:00:00+04:00 | 2020-05-05T10:00:00Z | DATETIME | 2020-05-03 10:00:00 | En este caso java utiliza el offset del timezone que tiene configurado para transformar el valor y almacenarlo en base de datos, debido a esto se recomienda configurar la aplicación para usar UTC|
| OffsetDateTime | 2020-05-05T10:00:00Z | 2020-05-05T10:00:00Z | DATETIME | 2020-05-03 10:00:00 | Igual que el caso anterior pero ya usamos UTC en el formato de entrada |


**IMPORTANTE**: Hay que tener en cuenta que si consultamos las bases de datos desde una herramienta puede que tengamos que configurarla para que no nos convierta los valores automáticamente antes de visualizarlos. Por ejemplo en Dbeaver hay que modificar su `.ini` y añadir `-Duser.timezone=UTC`

# Health check

Por defecto viene configurado Actuator para que solo tenga activa la url de heakth check en `http://HOST:PORT/actuator/health`, para mas información consultar la documentación de Spring Boot Actuator y sus properties.