[![Build Status](https://travis-ci.org/sgoeschl/freemarker-cli.svg?branch=master)](https://travis-ci.org/sgoeschl/freemarker-cli)

# 1. Is This Project For You?

You somehow found this GitHub project and wonder if it solves a problem you might have?!

* You need to transform some structured text document (CSV, HTML, JSON, XML, YAML, Java Property files, access logs) into CSV, HTML, Markdown or Confluence markup?
* You need to convert an Excel document into CSV, HTML or Markdown?
* You need to create a nice-looking PDF from some boring-looking CSV or JSON content ?

The goal of `freemarker-cli` is to automate repeated transformation tasks 

* Which are too boring to be done manually 
* Which happen not often enough to write a dedicated script or program

# 2. Once Upon A Time

In December 2015 I needed a little bit of test data management for a customer project - to make a long story short (after writing a few more Groovy scripts) it boiled down to transforming one or more JSON files to something human readable.

What are the options?

* The cool kids say 'Node.js' - but they always say 'Node.js' 
* Some fancy Groovy scripts using Groovy's markup builder - but the syntax looks a bit odd
* Using 'JsonPath' and 'Velocity' to reuse good & old stuff

So I went with 'Apache Groovy', 'JsonPath' and 'Apache Velocity'

* Playing with Groovy over the public holidays
* Groovy has a built-in package manager which makes distribution a breeze
* Providing samples to transform JSON to Markdown

Using Velocity actually created some minor issues so I migrated to [Apache FreeMarker](https://freemarker.apache.org) during Christmas 2016

* Velocity 1.7 was released 2010 and only recently there was a new release
* I was painful to get Velocity Tools working
* Velocity XML processing support is also painful
* Spring 4.3 deprecated Velocity support which could affect me in the long run
* FreeMarker has no additional dependencies and things are just working :-)

While I love Apache Velocity (Apache Turbine anyone?) I decided to give FreeMarker a chance and migrated my [velocity-cli](https://github.com/sgoeschl/velocity-cli) to FreeMarker.

Some years later the not-so-small-any-longer-and-not-having-tests Groovy script was still growing so I decided 

* To ditch Groovy and migrate to plain JDK 8
* Write unit tests since I had no more excuses
* To ditch Commons CLI and migrate to [Picocli](https://picocli.info)

# 3. Design Goals

* Support multiple source files/directories for a single transformation
* Support transformation of Property files using plain-vanilla JDK
* Support transformation of CSV files using [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/)
* Support transformation of JSON using [Jayway's JSONPath](https://github.com/jayway/JsonPath)
* Support transformation of Excel using [Apache POI](https://poi.apache.org)
* Support transformation of YAML using [SnakeYAML](https://bitbucket.org/asomov/snakeyaml/wiki/Home)
* Support transformation of HTML using [JSoup](https://jsoup.org)
* Support transformation of structured logfiles using [Grok](https://github.com/thekrakken/java-grok)
* XML is supported by FreeMarker [out-of-the-box](http://freemarker.org/docs/xgui.html)
* Support for reading document content from STDIN to integrate with command line tools
* Support execution of arbitrary commands using [Apache Commons Exec](https://commons.apache.org/proper/commons-exec/)
* Add some commonly useful information such as `System Properties`, `Enviroment Variables`
* Create a proper command-line tool which has Unix look & feel

# 4. Installation

Download the latest release from [GitHub](https://github.com/sgoeschl/freemarker-cli/releases), e.g. [freemarker-cli-2.0.0-BETA-4-app.tar.gz](https://github.com/sgoeschl/freemarker-cli/releases/download/v2.0.0-BETA-4/freemarker-cli-2.0.0-BETA-4-app.tar.gz) and unpack it into of a directory of your choice, e.g `/Application/Java/freemarker-cli`

It is recommended 

* To add `bin/freemarker-cli` or `bin/freemarker-cli.bat` to your executable path
* To create a `~/.freemarker-cli` directory to store your custom FTL templates

You can test the installation by executing

```text
> ./bin/freemarker-cli -t templates/info.ftl 
FreeMarker Information
---------------------------------------------------------------------------
FreeMarker version     : 2.3.29
Template name          : templates/info.ftl
Language               : en
Locale                 : en_AT
Timestamp              : Dec 10, 2019 10:06:46 PM
Output encoding        : UTF-8
Output format          : plainText

User Supplied Properties
---------------------------------------------------------------------------

Template Directories
---------------------------------------------------------------------------
[1] /Users/sgoeschl/work/github/sgoeschl/freemarker-cli/target/appassembler
[2] /Users/sgoeschl/.freemarker-cli

SystemTool
---------------------------------------------------------------------------
Host name       : XXXXXXXX.local
User name       : sgoeschl
Command line    : -t, templates/info.ftl

FreeMarker Document Model
---------------------------------------------------------------------------
- CSVTool
- Documents
- ExcelTool
- ExecTool
- FreeMarkerTool
- GrokTool
- JsonPathTool
- JsoupTool
- PropertiesTool
- SystemTool
- XmlTool
- YamlTool
```

There a many examples (see below) available you can execute - run `./run-samples.sh` and have a look at the generated output

```text
freemarker-cli-2.0.0> ./run-samples.sh 
templates/info.ftl
templates/demo.ftl
templates/csv/html/transform.ftl
templates/csv/md/transform.ftl
templates/csv/shell/curl.ftl
templates/csv/fo/transform.ftl
fop -fo target/out/locker-test-users.fo target/out/locker-test-users.pdf
templates/csv/fo/transactions.ftl
fop -fo target/out/transactions.fo target/out/transactions-fo.pdf
templates/csv/html/transform.ftl
wkhtmltopdf -O landscape target/out/transactions.html target/out/transactions-html.pdf
templates/accesslog/combined-access.ftl
templates/excel/html/transform.ftl
templates/excel/md/transform.ftl
templates/excel/csv/transform.ftl
templates/excel/csv/custom.ftl
templates/html/csv/dependencies.ftl
templates/json/csv/swagger-endpoints.ftl
templates/json/html/customer-user-products.ftl
wkhtmltopdf -O landscape target/out/customer-user-products.html target/out/customer-user-products.pdf
templates/json/md/customer-user-products.ftl
templates/json/md/github-users.ftl
templates/properties/csv/locker-test-users.ftl
templates/yaml/txt/transform.ftl
templates/xml/txt/recipients.ftl
Created the following sample files in ./target/out
total 1080
-rw-r--r--  1 sgoeschl  admin     646 Nov 12 19:38 combined-access.log.txt
-rw-r--r--  1 sgoeschl  admin   22548 Nov 12 19:38 contract.html
-rw-r--r--  1 sgoeschl  admin    7933 Nov 12 19:38 contract.md
-rw-r--r--  1 sgoeschl  admin     784 Nov 12 19:38 curl.sh
-rw-r--r--  1 sgoeschl  admin  103487 Nov 12 19:38 customer-user-products.html
-rw-r--r--  1 sgoeschl  admin   34990 Nov 12 19:39 customer-user-products.md
-rw-r--r--  1 sgoeschl  admin  114565 Nov 12 19:38 customer-user-products.pdf
-rw-r--r--  1 sgoeschl  admin     232 Nov 12 19:39 customer.txt
-rw-r--r--  1 sgoeschl  admin    5840 Nov 12 19:38 demo.txt
-rw-r--r--  1 sgoeschl  admin    1310 Nov 12 19:38 dependencies.csv
-rw-r--r--  1 sgoeschl  admin    2029 Nov 12 19:39 github-users-curl.md
-rw-r--r--  1 sgoeschl  admin    1335 Nov 12 19:38 info.txt
-rw-r--r--  1 sgoeschl  admin     239 Nov 12 19:39 locker-test-users.csv
-rw-r--r--  1 sgoeschl  admin    6288 Nov 12 19:38 locker-test-users.fo
-rw-r--r--  1 sgoeschl  admin    5488 Nov 12 19:38 locker-test-users.pdf
-rw-r--r--  1 sgoeschl  admin     921 Nov 12 19:39 recipients.txt
-rw-r--r--  1 sgoeschl  admin     341 Nov 12 19:38 swagger-spec.csv
-rw-r--r--  1 sgoeschl  admin     156 Nov 12 19:38 test-multiple-sheets.xlsx.csv
-rw-r--r--  1 sgoeschl  admin    1917 Nov 12 19:38 test-multiple-sheets.xlsx.html
-rw-r--r--  1 sgoeschl  admin     389 Nov 12 19:38 test-multiple-sheets.xlsx.md
-rw-r--r--  1 sgoeschl  admin     150 Nov 12 19:38 test-transform-xls.csv
-rw-r--r--  1 sgoeschl  admin    1556 Nov 12 19:38 test.xls.html
-rw-r--r--  1 sgoeschl  admin    1558 Nov 12 19:38 test.xslx.html
-rw-r--r--  1 sgoeschl  admin   11334 Nov 12 19:38 transactions-fo.pdf
-rw-r--r--  1 sgoeschl  admin   33235 Nov 12 19:38 transactions-html.pdf
-rw-r--r--  1 sgoeschl  admin  106440 Nov 12 19:38 transactions.fo
-rw-r--r--  1 sgoeschl  admin   18126 Nov 12 19:38 transactions.html
```

Please note that generated PDF files are very likely not found since they require `wkhtmltopdf` and `Apache FOP` installation.

# 5. Usage

```text
> ./bin/freemarker-cli -h
Usage: freemarker-cli [-EhV] [--stdin] [-b=<baseDir>] [--config=<configFile>]
                      [-e=<inputEncoding>] [--include=<include>] [-l=<locale>]
                      [-o=<outputFile>] [--output-encoding=<outputEncoding>]
                      -t=<template> [--times=<times>] [-D=<String=String>]...
                      [<sources>...]
Apache FreeMarker CLI
      [<sources>...]        List of input files and/or input directories
  -b, --basedir=<baseDir>   Optional template base directory
      --config=<configFile> FreeMarker CLI configuration file
  -D, --property=<String=String>
                            Set system property
  -e, --input-encoding=<inputEncoding>
                            Encoding of input file
  -E, --expose-env          Expose environment variables and user-supplied
                              properties globally
  -h, --help                Show this help message and exit.
      --include=<include>   File pattern for input directory
  -l, --locale=<locale>     Locale being used for output file, e.g. 'en_US'
  -o, --output=<outputFile> Output file
      --output-encoding=<outputEncoding>
                            Encoding of output file, e.g. UTF-8
      --stdin               Read input document from stdin
  -t, --template=<template> FreeMarker template to render
      --times=<times>       Re-run X times for profiling
  -V, --version             Print version information and exit.
```

# 6. Examples

The examples were tested with JDK 1.8 on Mac OS X

```text
> java -version
java version "1.8.0_192"
Java(TM) SE Runtime Environment (build 1.8.0_192-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.192-b12, mixed mode)
```
It is assumed that you run the examples from the `freemarker-cli` installation directory.

## 6.1 Transforming GitHub JSON To Markdown

A simple example with real JSON data

### Invocation

You can either use the existing JSON sample

> ./bin/freemarker-cli -t templates/json/md/github-users.ftl site/sample/json/github-users.json

or pipe a cURL response

> curl -s https://api.github.com/users | ./bin/freemarker-cli -t templates/json/md/github-users.ftl --stdin

### FreeMarker Template

```text
<#ftl output_format="plainText" >
<#assign json = JsonPathTool.parse(Documents.get(0))>
<#assign users = json.read("$[*]")>
<#--------------------------------------------------------------------------->
# GitHub Users

Report generated at ${.now?iso_utc}

<#compress>
<#list users as user>
<#assign userAvatarUrl = user.avatar_url>
<#assign userHomeUrl = user.html_url>
# ${user.login}

| User                                                    | Homepage                                      |
|:--------------------------------------------------------|:----------------------------------------------|
| <img src="${user.avatar_url}" width="48" height="48"/> | [${userHomeUrl}](${userHomeUrl})               |
</#list>
</#compress>
```

creates the following output

![](./site/image/github.png)

## 6.2 Markdown Test User Documentation

For a customer I created a Groovy script to fetch all products for a list of users - the script generates a JSON file which can be easily transformed to Markdown

```text
> ./bin/freemarker-cli -t templates/json/md/customer-user-products.ftl  site/sample/json/customer-user-products.json
```

The resulting file can be viewed with any decent Markdown viewer

![Customer User Products Markdown](./site/image/customer-user-products-md.png "Customer User Products Markdown")

Since many of our QA people have no Markdown viewer installed I also created a very similar HTML representaton

> ./bin/freemarker-cli -t templates/json/html/customer-user-products.ftl  site/sample/json/customer-user-products.json

![Customer User Products HTML](./site/image/customer-user-products-html.png "Customer User Products HTML")

As added bonus we can easily transform the HTML to PDF using [wkhtmltopdf](https://wkhtmltopdf.org)

```text
freemarker-cli> wkhtmltopdf ./site/sample/customer-user-products.html customer-user-products.pdf
Loading pages (1/6)
Counting pages (2/6)                                               
Resolving links (4/6)                                                       
Loading headers and footers (5/6)                                           
Printing pages (6/6)
Done  
```

which creates the following PDF document (please note that even the links within the document are working)

![Customer User Products PDF](./site/image/customer-user-products-pdf.png "Customer User Products PDF")

## 6.3 CSV to HTML/Markdown Transformation

Sometimes you have a CSV file which needs to be translated in Markdown or HTML - there are on-line solutions available such as [CSV To Markdown Table Generator](https://donatstudios.com/CsvToMarkdownTable) but having a local solution gives you more flexibility.

```text
> ./bin/freemarker-cli -t templates/csv/md/transform.ftl site/sample/csv/contract.csv
> ./bin/freemarker-cli -t templates/csv/html/transform.ftl site/sample/csv/contract.csv
```

The FreeMarker template is shown below

```text
<#ftl output_format="HTML" >
<#assign document = Documents.get(0)>
<#assign name = document.name>
<#assign cvsFormat = CSVTool.formats["DEFAULT"].withHeader()>
<#assign csvParser = CSVTool.parse(document, cvsFormat)>
<#assign csvHeaders = csvParser.getHeaderNames()>
<#--------------------------------------------------------------------------->
<!DOCTYPE html>
<html>
<head>
    <title>${name}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
</head>
<body>
<table class="table table-striped">
    <@writeHeaders csvParser.getHeaderNames()/>
    <#list csvParser.iterator() as record>
        <@writeColumns record/>
    </#list>
</table>
</body>
</html>
<#--------------------------------------------------------------------------->
<#macro writeHeaders headers>
    <tr>
        <#list headers as header>
            <th>${header}</th>
        </#list>
    </tr>
</#macro>
<#--------------------------------------------------------------------------->
<#macro writeColumns record>
    <tr>
        <#list record.iterator() as field>
            <th>${field}</th>
        </#list>
    </tr>
</#macro>

```

The resulting file actually looks pleasant when compared to raw CSV

![Contract CSV to HTML](./site/image/contract.png "Contract CSV to HTML")

## 6.4 Transform XML To Plain Text

Of course you can also transform a XML document

```text
> ./bin/freemarker-cli -t ./templates/xml/txt/recipients.ftl site/sample/xml/recipients.xml
```

using the following template

```text
<#ftl output_format="plainText" >
<#assign xml = XmlTool.parse(Documents.get(0))>
<#list xml.recipients.person as recipient>
To: ${recipient.name}
${recipient.address}

Dear ${recipient.name},

Thank you for your interest in our products. We will be sending you a catalog shortly.
To take advantage of our free gift offer, please fill in the survey attached to this
letter and return it to the address on the reverse. Only one participant is allowed for
each household.

Sincere salutations,


D. H.

---------------------------------------------------------------------------------------
</#list>

```

which generates the following output

```text
To: John Smith
3033 Long Drive, Houston, TX

Dear John Smith,

Thank you for your interest in our products. We will be sending you a catalog shortly.
To take advantage of our free gift offer, please fill in the survey attached to this
letter and return it to the address on the reverse. Only one participant is allowed for
each household.

Sincere salutations,


D. H.
```

## 6.5 Transform JSON To CSV

One day I was asked a to prepare a CSV files containing REST endpoints described by Swagger - technically this is a JSON to CSV transformation. Of course I could create that CSV manually but writing a FTL template doing that was simply more fun and saves time in the future.

```text
<#ftl output_format="plainText" strip_text="true">
<#assign json = JsonPathTool.parse(Documents.get(0))>
<#assign basePath = json.read("$.basePath")>
<#assign paths = json.read("$.paths")>

<#compress>
    ENDPOINT;METHOD;DESCRIPTION
    <#list paths as endpoint,metadata>
        <#assign relative_url = basePath + endpoint>
        <#assign methods = metadata?keys>
        <#list methods as method>
            <#assign description = paths[endpoint][method]["description"]?replace(";", ",")>
            ${relative_url};${method?upper_case};${description}
        </#list>
    </#list>
</#compress>
${'\n'}

```

Invoking the FTL template

> ./bin/freemarker-cli -t templates/json/csv/swagger-endpoints.ftl site/sample/json/swagger-spec.json 

gives you

```text
ENDPOINT;METHOD;DESCRIPTION
/api/pets;GET;Returns all pets from the system that the user has access to
/api/pets;POST;Creates a new pet in the store. Duplicates are allowed
/api/pets/{id};GET;Returns a user based on a single ID, if the user does not have access to the pet
/api/pets/{id};DELETE;Deletes a single pet based on the ID supplied
```

## 6.6 Transforming Excel Documents

Another day my project management asked me to create a CSV configuration file based on an Excel documents - as usual manual copying was not an option due to required data cleanup and data transformation. So I thought about Apache POI which support XLS and XLSX documents - integration of Apache POI was a breeze but the resulting code was not particularly useful example. So a more generic transformation was provided to show the transformation of Excel documents ...

```text
> ./bin/freemarker-cli -t templates/excel/html/transform.ftl site/sample/excel/test.xls
> ./bin/freemarker-cli -t templates/excel/html/transform.ftl site/sample/excel/test.xlsx
> ./bin/freemarker-cli -t templates/excel/html/transform.ftl site/sample/excel/test-multiple-sheets.xlsx
> ./bin/freemarker-cli -t templates/excel/md/transform.ftl site/sample/excel/test-multiple-sheets.xlsx
```

The provided FTL transforms an Excel into a HTML document supporting multiple Excel sheets

```text
<#ftl output_format="HTML" >
<#assign document = Documents.get(0)>
<#assign documentName = document.name>
<#assign workbook = ExcelTool.parse(document)>
<#assign date = .now?iso_utc>
<#--------------------------------------------------------------------------->
<!DOCTYPE html>
<html>
<head>
    <title>${documentName}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
</head>
<body>
<div class="container-fluid">
    <h1>Excel Test
        <small>${documentName}, ${date}</small>
    </h1>
    <@writeSheets workbook/>
</div>
</body>
</html>

<#--------------------------------------------------------------------------->
<#-- writeSheets                                                           -->
<#--------------------------------------------------------------------------->
<#macro writeSheets workbook>
    <#assign sheets = ExcelTool.getSheets(workbook)>
    <#list sheets as sheet>
        <@writeSheet sheet/>
    </#list>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeSheet                                                            -->
<#--------------------------------------------------------------------------->
<#macro writeSheet sheet>
    <#assign rows = ExcelTool.parseSheet(sheet)>
    <h2>${sheet.getSheetName()}</h2>
    <@writeRows rows/>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeRows                                                             -->
<#--------------------------------------------------------------------------->
<#macro writeRows rows>
    <table class="table table-striped">
        <#list rows as row>
            <#if row?is_first>
                <tr>
                    <th>#</th>
                    <#list row as column>
                        <th>${column}</th>
                    </#list>
                </tr>
            <#else>
                <tr>
                    <td>${row?index}</td>
                    <#list row as column>
                        <td>${column}</td>
                    </#list>
                </tr>
            </#if>
        </#list>
    </table>
</#macro>
```

but the result looks reasonable

![](./site/image/excel-to-html.png)

## 6.7 Transform Property Files To CSV

In this sample we transform all property files found in a directory (recursive search using include pattern) to a CSV file

```text
> ./bin/freemarker-cli --include *.properties -t templates/properties/csv/locker-test-users.ftl site/sample/properties
TENANT,SITE,USER_ID,DISPOSER_ID,PASSWORD,SMS_OTP,NAME,DESCRIPTION
???,fat,01303494,01303494,01303494,,,
???,fat,9422350309,9422350309,9422350309,000000,,
???,fat,205089760,205089760,205089760,,,
???,uat,9200021464,9200021464,9200021464,,,
```

The FTL uses a couple of interesting features

* We process a list of property files
* The `strip_text` and `compress` strips any white-spaces and line-breaks from the output so we can create a proper CSV file
* We use FTL functions to extract the `tenant` and `site`, e.g. `extractTenant`
* We add a manual line break using ```${'\n'}```

```text
<#ftl output_format="plainText" strip_text="true">
<#compress>
    TENANT,SITE,USER_ID,DISPOSER_ID,PASSWORD,SMS_OTP,NAME,DESCRIPTION
    <#list Documents.list as document>
        <#assign properties = PropertiesTool.parse(document)>
        <#assign environments = properties["ENVIRONMENTS"]!"">
        <#assign tenant = extractTenant(environments)>
        <#assign site = extractSite(environments)>
        <#assign userId = properties["USER_ID"]!"">
        <#assign disposerId = properties["USER_ID"]!"">
        <#assign password = properties["PASSWORD"]!"">
        <#assign smsOtp = properties["SMS_OTP"]!"">
        <#assign name = properties["NAME"]!"">
        <#assign description = properties["NAME"]!"">
        ${tenant},${site},${userId},${disposerId},${password},${smsOtp},${name},${description}
    </#list>
</#compress>
${'\n'}

<#function extractSite environments>
</#function>

<#function extractTenant environments>
</#function>

```

## 5.8 Transform CSV To XML-FO

For a POC (proof of concept) I created a sample transformation from CSV to XML-FO in order to create a PDF document using [Apache FOP](https://xmlgraphics.apache.org/fop) using the following template file

```text
<#ftl output_format="XML" >
<#assign document = Documents.get(0)>
<#assign name = document.name>
<#assign cvsFormat = CSVTool.formats.DEFAULT.withDelimiter('\t').withHeader()>
<#assign csvParser = CSVTool.parse(document, cvsFormat)>
<#assign csvHeaders = csvParser.getHeaderMap()?keys>
<#assign csvRecords = csvParser.records>
<#--------------------------------------------------------------------------->
<?xml version="1.0" encoding="UTF-8"?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="21cm"
                               page-width="29.7cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            <fo:region-body margin-top="1cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="-1.2cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    <fo:page-sequence master-reference="first">
        <fo:static-content flow-name="xsl-region-before">
            <fo:block line-height="10pt" font-size="8pt" text-align="left">Transaction Export - ${.now}</fo:block>
        </fo:static-content>
        <fo:static-content flow-name="xsl-region-after">
            <fo:block line-height="6pt" font-size="6pt" text-align="end">Page <fo:page-number/></fo:block>
        </fo:static-content>
        <fo:flow flow-name="xsl-region-body">
            <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                <fo:table-column column-width="8%"/>
                <fo:table-column column-width="10%"/>
                <fo:table-column column-width="12%"/>
                <fo:table-column column-width="8%"/>
                <fo:table-column column-width="7%"/>
                <fo:table-column column-width="5%"/>
                <fo:table-column column-width="5%"/>
                <fo:table-column column-width="5%"/>
                <fo:table-column column-width="35%"/>
                <fo:table-column column-width="5%"/>
                <@writeTableHeader headers=csvHeaders/>
                <@writeTableBody columns=csvRecords/>
            </fo:table>
        </fo:flow>
    </fo:page-sequence>
</fo:root>

<#--------------------------------------------------------------------------->
<#macro writeTableHeader headers>
    <fo:table-header>
        <fo:table-row>
            <#list headers as header>
                <fo:table-cell border-style="solid" border-width="0.1pt" padding-left="1.0px" padding-right="1.0px">
                    <fo:block font-size="6pt" font-weight="bold">${header}</fo:block>
                </fo:table-cell>
            </#list>
        </fo:table-row>
    </fo:table-header>
</#macro>

<#--------------------------------------------------------------------------->
<#macro writeTableBody columns>
    <fo:table-body>
        <#list columns as column>
            <fo:table-row>
                <#list column.iterator() as field>
                    <fo:table-cell border-style="solid" border-width="0.1pt" padding-left="1.0px" padding-right="1.0px">
                        <fo:block font-size="6pt">${field}</fo:block>
                    </fo:table-cell>
                </#list>
            </fo:table-row>
        </#list>
    </fo:table-body>
</#macro>

```

In order to create the PDF you need to execute the following commands (assuming that you have Apache FOP installed)

```text
> ./bin/freemarker-cli -t templates/csv/fo/transform.ftl site/sample/csv/locker-test-users.csv > sample.fo
> fop -fo sample.fo sample.pdf
Dec 29, 2018 10:24:30 PM org.apache.fop.events.LoggingEventListener processEvent
WARNING: Font "Symbol,normal,700" not found. Substituting with "Symbol,normal,400".
Dec 29, 2018 10:24:30 PM org.apache.fop.events.LoggingEventListener processEvent
WARNING: Font "ZapfDingbats,normal,700" not found. Substituting with "ZapfDingbats,normal,400".
Dec 29, 2018 10:24:30 PM org.apache.fop.events.LoggingEventListener processEvent
INFO: Rendered page #1.
```

The result does not look very impressive but it is a PDF :-)

![](./site/image/locker-test-users-pdf.png)

Further along the line of the POC we converted a transaction export from CSV to PDF using Apache FOP

```text
> ./bin/freemarker-cli -t templates/csv/fo/transactions.ftl site/sample/csv/transactions.csv > transactions.fo
> fop -fo transactions.fo transactions.pdf
Jan 16, 2019 11:15:21 PM org.apache.fop.events.LoggingEventListener processEvent
WARNING: Font "Symbol,normal,700" not found. Substituting with "Symbol,normal,400".
Jan 16, 2019 11:15:21 PM org.apache.fop.events.LoggingEventListener processEvent
WARNING: Font "ZapfDingbats,normal,700" not found. Substituting with "ZapfDingbats,normal,400".
Jan 16, 2019 11:15:21 PM org.apache.fop.events.LoggingEventListener processEvent
WARNING: The contents of fo:block line 1 exceed the available area in the inline-progression direction by 11027 millipoints. (See position 1519:51)
Jan 16, 2019 11:15:22 PM org.apache.fop.events.LoggingEventListener processEvent
INFO: Rendered page #1.
Jan 16, 2019 11:15:22 PM org.apache.fop.events.LoggingEventListener processEvent
INFO: Rendered page #2.
```

![](./site/image/transactions.png)

## 6.9 Transforming HTML To CSV

Recently I got the rather unusual question how to determine the list of dependencies of an application - one easy way is the Maven "dependencies.html" but this is unstructured data. Having said that the Jsoup library is perfectly able to parse most real-life HTML and provides a DOM model

```text
<#ftl output_format="plainText" strip_text="true">
<#assign document = Documents.get(0)>
<#assign documentName = document.name>
<#assign html = JsoupTool.parse(document)>

<#compress>
    <@writeHeader/>
    <@writeDependencies "Project_Dependencies_compile"/>
    <@writeDependencies "Project_Transitive_Dependencies_compile"/>
    <@writeDependencies "Project_Transitive_Dependencies_runtime"/>
    <@writeDependencies "Project_Transitive_Dependencies_provided"/>
</#compress>

<#macro writeHeader>
    GroupId,ArtifactId,Version,Type,Licenses
</#macro>

<#macro writeDependencies section>
    <#assign selection = html.select("a[name=${section}]")>
    <#if selection?has_content>
        <#assign table = selection[0].nextElementSibling().child(2).child(0)>
        <#assign rows = table.children()>
        <#list rows as row>
            <#if !row?is_first>
                <#assign groupId = row.child(0).text()>
                <#assign artificatId = row.child(1).text()>
                <#assign version = row.child(2).text()>
                <#assign type = row.child(3).text()>
                <#assign licences = row.child(4).text()?replace(",", "")>
                ${groupId},${artificatId},${version},${type},${licences}
            </#if>
        </#list>
    </#if>
</#macro>


```

Your dependencies as CSV can be generated as shown below

```text
> ./bin/freemarker-cli -t templates/html/csv/dependencies.ftl site/sample/html/dependencies.html 
GroupId,ArtifactId,Version,Type,Licenses
com.jayway.jsonpath,json-path,2.4.0,jar,The Apache Software License Version 2.0
commons-cli,commons-cli,1.4,jar,Apache License Version 2.0
org.apache.commons,commons-csv,1.5,jar,Apache License Version 2.0
org.apache.poi,poi,4.0.1,jar,The Apache Software License Version 2.0
org.apache.poi,poi-ooxml,3.17,jar,The Apache Software License Version 2.0
org.apache.poi,poi-ooxml-schemas,3.17,jar,The Apache Software License Version 2.0
org.freemarker,freemarker,2.3.28,jar,Apache License Version 2.0
org.jsoup,jsoup,1.11.3,jar,The MIT License
org.slf4j,slf4j-api,1.7.21,jar,MIT License
org.slf4j,slf4j-log4j12,1.7.21,jar,MIT License
com.github.virtuald,curvesapi,1.04,jar,BSD License
commons-codec,commons-codec,1.11,jar,Apache License Version 2.0
log4j,log4j,1.2.17,jar,The Apache Software License Version 2.0
net.minidev,accessors-smart,1.2,jar,The Apache Software License Version 2.0
net.minidev,json-smart,2.3,jar,The Apache Software License Version 2.0
org.apache.commons,commons-collections4,4.2,jar,Apache License Version 2.0
org.apache.commons,commons-math3,3.6.1,jar,Apache License Version 2.0
org.apache.xmlbeans,xmlbeans,2.6.0,jar,The Apache Software License Version 2.0
org.ow2.asm,asm,5.0.4,jar,BSD
stax,stax-api,1.0.1,jar,The Apache Software License Version 2.0
```

## 6.10 Transform CSV To Shell Script

For a customer project we wanted to record REST request / responses using WireMock - really quick and dirty. So we decided to avoid any sophisticated test tool but generate a ready-to-use shell script executing cURL commands. It turned out that handling of dollar signs is a bit tricky

* Using ```noparse``` directive to disable parsing of dollar signs
* Using ```${r"${MY_BASE_URL}"``` to generate output with dollar signs

and the final FTL is found below

```
<#ftl output_format="plainText">
<#assign cvsFormat = CSVTool.formats["DEFAULT"].withHeader()>
<#assign csvParser = CSVTool.parse(Documents.get(0), cvsFormat)>
<#assign records = csvParser.records>
<#assign csvMap = CSVTool.toMap(records, "disposer")>
<#--------------------------------------------------------------------------->
#!/bin/sh

<#noparse>
MY_BASE_URL=${MY_BASE_URL:=https://postman-echo.com}
</#noparse>
 
echo "time,user,status,duration,size"
<#list records as record>
date "+%FT%H:%M:%S" | tr -d '\n'; curl --write-out ',${record.disposer},%{http_code},%{time_total},%{size_download}\n' --silent --show-error --output /dev/null "${r"${MY_BASE_URL}"}/get"
</#list>
```

Rendering the FreeMarker template 

```
> ./bin/freemarker-cli -t ./templates/csv/shell/curl.ftl site/sample/csv/user.csv
```

generates the following shell script

```
#!/bin/sh

MY_BASE_URL=${MY_BASE_URL:=https://postman-echo.com}
 
echo "time,user,status,duration,size"
date "+%FT%H:%M:%S" | tr -d '\n'; curl --write-out ',AAAAAAA,%{http_code},%{time_total},%{size_download}\n' --silent --show-error --output /dev/null "${MY_BASE_URL}/get"
date "+%FT%H:%M:%S" | tr -d '\n'; curl --write-out ',BBBBBBB,%{http_code},%{time_total},%{size_download}\n' --silent --show-error --output /dev/null "${MY_BASE_URL}/get"
date "+%FT%H:%M:%S" | tr -d '\n'; curl --write-out ',CCCCCCC,%{http_code},%{time_total},%{size_download}\n' --silent --show-error --output /dev/null "${MY_BASE_URL}/get"
date "+%FT%H:%M:%S" | tr -d '\n'; curl --write-out ',DDDDDDD,%{http_code},%{time_total},%{size_download}\n' --silent --show-error --output /dev/null "${MY_BASE_URL}/get"
```

Looks a bit complicated but lets dissect the things

* `date "+%FT%H:%M:%S" | tr -d '\n'` creates a timestamp and removes the line feed
* `curl --write-out` allows to print runtime data (see [https://ec.haxx.se/usingcurl-writeout.html](https://ec.haxx.se/usingcurl-writeout.html))

Executing the result shell script creates the following output (which is a nice CSV for further processing)

```
time,user,status,duration,size
2019-09-27T21:02:52,AAAAAAA,200,0.522473,206
2019-09-27T21:02:53,BBBBBBB,200,0.498093,206
2019-09-27T21:02:54,CCCCCCC,200,0.529013,206
2019-09-27T21:02:54,DDDDDDD,200,0.528268,206
```

## 6.11 Unleashing The Power Of Grok

Think of `Grok` as modular regular expressions with a pre-defined functionality to parse access logs or any other data where you can't comprehend the regular expression any longer, one very simple example is `QUOTEDSTRING`

```
QUOTEDSTRING (?>(?<!\\)(?>"(?>\\.|[^\\"]+)+"|""|(?>'(?>\\.|[^\\']+)+')|''|(?>`(?>\\.|[^\\`]+)+`)|``))
```

And with `Grok` the `QUOTEDSTRING` is just a building block for an even more complex regular expession such as `COMBINEDAPACHELOG`

> bin/freemarker-cli -t templates/accesslog/combined-access.ftl site/sample/accesslog/combined-access.log 

which gives you the following output

```
TIMESTAMP;VERB;REQUEST;HTTPVERSION
19/Jun/2005:06:44:17 +0200;GET;/wximages/wxwidgets02-small.png;1.1
19/Jun/2005:06:46:05 +0200;GET;/wximages/wxwidgets02-small.png;1.1
19/Jun/2005:06:47:37 +0200;GET;/wximages/wxwidgets02-small.png;1.1
19/Jun/2005:06:48:40 +0200;GET;/wiki.pl?WxWidgets_Bounties;1.1
19/Jun/2005:06:50:49 +0200;GET;/wiki.pl?WxWidgets_Compared_To_Other_Toolkits;1.1
19/Jun/2005:06:50:49 +0200;GET;/wxwiki.css;1.1
19/Jun/2005:06:50:49 +0200;GET;/wximages/wxwidgets02-small.png;1.1
19/Jun/2005:06:50:50 +0200;GET;/favicon.ico;1.1
19/Jun/2005:06:52:36 +0200;GET;/wximages/wxwidgets02-small.png;1.1
19/Jun/2005:06:53:14 +0200;GET;/;1.0
```

using the following FreeMarker template

```
<#ftl output_format="plainText" strip_whitespace=true>
<#assign grok = GrokTool.compile("%{COMBINEDAPACHELOG}")>
<#assign document = Documents.get(0)>
<#assign lines = document.getLineIterator()>

<#compress>
    TIMESTAMP;VERB;REQUEST;HTTPVERSION
    <#list lines as line>
        <#assign parts = grok.match(line)>
        <#assign timestamp = parts["timestamp"]>
        <#assign verb = parts["verb"]>
        <#assign request = parts["request"]>
        <#assign httpversion = parts["httpversion"]>
        ${timestamp};${verb};${request};${httpversion}
    </#list>
</#compress>
```

While this looks small and tidy there are some nifty features

* `GrokTool.compile("%{COMBINEDAPACHELOG}")` builds the `Grok` instance to parse access logs in `Combined Format`
* The source document is streamed line by line and not loaded into memory in one piece
* This also works for using `stdin` so are able to parse GB of access log or other files

## 6.12 CSV Transformation

Sometimes you have a CSV file which is not quite right - you need to change the format. Lets have a look how `freemarker-cli` can help

> bin/freemarker-cli -Dcsv.in.delimiter=COMMA -Dcsv.out.delimiter=PIPE -t templates/csv/transform.ftl ./site/sample/csv/contract.csv 

renders the following template

```
<#ftl output_format="plainText" strip_text="true">
<#assign csvParser = createCsvParser(Documents.get(0))>
<#assign csvPrinter = createCsvPrinter()>
<#-- Print each line without materializing the CSV in memory -->
<#compress>
    <#list csvParser.iterator() as record>
        ${csvPrinter.printRecord(record)}
    </#list>
</#compress>

<#function createCsvParser document>
    <#assign initialCvsInFormat = CSVTool.formats[SystemTool.getProperty("csv.in.format", "DEFAULT")]>
    <#assign csvInDelimiter = CSVTool.toDelimiter(SystemTool.getProperty("csv.in.delimiter", initialCvsInFormat.getDelimiter()))>
    <#assign cvsInFormat = initialCvsInFormat.withDelimiter(csvInDelimiter)>
    <#return CSVTool.parse(document, cvsInFormat)>
</#function>

<#function createCsvPrinter>
    <#assign initialCvsOutFormat = CSVTool.formats[SystemTool.getProperty("csv.out.format", "DEFAULT")]>
    <#assign csvOutDelimiter = CSVTool.toDelimiter(SystemTool.getProperty("csv.out.delimiter", initialCvsOutFormat.getDelimiter()))>
    <#assign cvsOutFormat = initialCvsOutFormat.withDelimiter(csvOutDelimiter)>
    <#return CSVTool.printer(cvsOutFormat)>
</#function>
```

and generates

```text
contract_id|seller_company_name|customer_company_name|customer_duns_number|contract_affiliate|FERC_tariff_reference|contract_service_agreement_id|contract_execution_date|contract_commencement_date|contract_termination_date|actual_termination_date|extension_provision_description|class_name|term_name|increment_name|increment_peaking_name|product_type_name|product_name|quantity|units_for_contract|rate|rate_minimum|rate_maximum|rate_description|units_for_rate|point_of_receipt_control_area|point_of_receipt_specific_location|point_of_delivery_control_area|point_of_delivery_specific_location|begin_date|end_date|time_zone
C71|The Electric Company|The Power Company|456543333|N|FERC Electric Tariff Original Volume No. 10|2|2/15/2001|2/15/2001|||Evergreen|N/A|N/A|N/A|N/A|MB|ENERGY|0||" "|" "|" "|Market Based||||||||ES
C72|The Electric Company|Utility A|38495837|n|FERC Electric Tariff Original Volume No. 10|15|7/25/2001|8/1/2001|||Evergreen|N/A|N/A|N/A|N/A|MB|ENERGY|0||" "|" "|" "|Market Based||||||||ES
C73|The Electric Company|Utility B|493758794|N|FERC Electric Tariff Original Volume No. 10|7|6/8/2001|7/6/2001|||Evergreen|N/A|N/A|N/A|N/A|MB|ENERGY|0||" "|" "|" "|Market Based||||" "|" "|||ep
C74|The Electric Company|Utility C|594739573|n|FERC Electric Tariff Original Volume No. 10|25|6/8/2001|7/6/2001|||Evergreen|N/A|N/A|N/A|N/A|MB|ENERGY|0||" "|" "|" "|Market Based||||" "|" "|||ep
```

Some useful hints

* For available CSV formats please see [Apache Commons CSV User Guide](http://commons.apache.org/proper/commons-csv/user-guide.html)
* Stripping the Excel BOM (Byte Order Mark) works out-of-box

## 6.13 Executing Arbitrary Commands

Using Apache Commons Exec allows to execute arbitrary commands - nice but dangerous. It was recently quite useful to to invoke AWS CLI to generate a Confluence page about the overall setup of our AWS accounts. 

A few snippets to illustrate the points

```
<#ftl output_format="plainText" strip_whitespace="true">
<#assign profile = SystemTool.getProperty("profile", "default")>
<#assign ec2Instances = ec2Instances()/>

h3. AWS EC2 Instance
<@printEc2Instances ec2Instances/>

<#function ec2Instances>
    <#local json = awsCliToJson("aws ec2 describe-instances --profile ${profile}")>
    <#local instances = json.read("$.Reservations[*].Instances[*]")>
    <#return instances?sort_by(['InstanceType'])>
</#function>

<#function awsCliToJson line>
    <#local output = ExecTool.execute(line)>
    <#return JsonPathTool.parse(output)>
</#function>

<#function getAwsEc2InstanceTag tags name>
    <#return tags?filter(x -> x["Key"] == name)?first["Value"]!"">
</#function>

<#macro printEc2Instances ec2Instances>
    <#compress>
        || NAME || INSTANCE_TYPE || VCPUS || STATE || PRIVATE_IP_ADDRESS ||
        <#list ec2Instances as ec2Instance>
            <#assign instanceType = ec2Instance["InstanceType"]>
            <#assign arn = ec2Instance["IamInstanceProfile"]["Arn"]>
            <#assign privateIpAddress = ec2Instance["PrivateIpAddress"]>
            <#assign state = ec2Instance["State"]["Name"]>
            <#assign launchTime = ec2Instance["LaunchTime"]>

            <#assign coreCount = ec2Instance["CpuOptions"]["CoreCount"]?number>
            <#assign threadsPerCore = ec2Instance["CpuOptions"]["ThreadsPerCore"]?number>
            <#assign nrOfVirtualCpus = coreCount * threadsPerCore>

            <#assign tags = ec2Instance["Tags"]/>
            <#assign awsCloudFormationStackId = getAwsEc2InstanceTag(tags, "aws:cloudformation:stack-id")>
            <#assign awsCloudFormationStackName = getAwsEc2InstanceTag(tags, "aws:cloudformation:stack-name")>
            <#assign name = getAwsEc2InstanceTag(tags, "Name")>
            <#assign country = getAwsEc2InstanceTag(tags, "Country")>
            <#assign environment = getAwsEc2InstanceTag(tags, "Environment")>

            | ${name} | ${instanceType} | ${nrOfVirtualCpus} | ${state} | ${privateIpAddress} |
        </#list>
    </#compress>
</#macro>
```

## 6.14 Using Advanced FreeMarker Features

There is a `demo.ftl` which shows some advanced FreeMarker functionality

* Invoking a Java constructor
* Invoke a static method of non-instantiable class
* Work with Java enumerations
* Access System properties
* Access Environment variables

Running 

> ./bin/freemarker-cli -t templates/demo.ftl 

gives you

```text
<#ftl output_format="plainText">
1) FreeMarker Special Variables
---------------------------------------------------------------------------
FreeMarker version     : ${.version}
Template name          : ${.current_template_name}
Language               : ${.lang}
Locale                 : ${.locale}
Timestamp              : ${.now}
Output encoding        : ${.output_encoding!"not set"}
Output format          : ${.output_format}

2) Invoke a constructor of a Java class
---------------------------------------------------------------------------
<#assign date = FreeMarkerTool.objectConstructor("java.util.Date", 1000 * 3600 * 24)>
new java.utilDate(1000 * 3600 * 24): ${date?datetime}

3) Invoke a static method of an non-constructor class
---------------------------------------------------------------------------
System.currentTimeMillis: ${FreeMarkerTool.statics["java.lang.System"].currentTimeMillis()}

4) Access an Enumeration
---------------------------------------------------------------------------
java.math.RoundingMode#UP: ${FreeMarkerTool.enums["java.math.RoundingMode"].UP}

5) Loop Over The Values Of An Enumeration
---------------------------------------------------------------------------
<#list FreeMarkerTool.enums["java.math.RoundingMode"]?values as roundingMode>
    - java.math.RoundingMode.${roundingMode}<#lt>
</#list>

6) Display list of input files
---------------------------------------------------------------------------
List all files:
<#list Documents.list as document>
    - Document: name=${document.name} location=${document.location} length=${document.length} encoding=${document.encoding!""}
</#list>

7) SystemTool
---------------------------------------------------------------------------
Host name       : ${SystemTool.getHostName()}
Command line    : ${SystemTool.getArgs()?join(", ")}
User name       : ${SystemTool.getProperty("user.name", "N.A.")}
Timestamp       : ${SystemTool.currentTimeMillis()?c}
Environment     : ${SystemTool.envs["FOO"]!"N.A."}

8) Access System Properties
---------------------------------------------------------------------------
app.dir      : ${SystemTool.properties["app.dir"]!""}
app.home     : ${SystemTool.properties["app.home"]!""}
app.pid      : ${SystemTool.properties["app.pid"]!""}
basedir      : ${SystemTool.properties["basedir"]!""}
java.version : ${SystemTool.properties["java.version"]!""}
user.name    : ${SystemTool.properties["user.name"]!""}
user.dir     : ${SystemTool.properties["user.dir"]!""}
user.home    : ${SystemTool.properties["user.home"]!""}

9) Acessing Environment Variables
---------------------------------------------------------------------------
<#list SystemTool.envs as name,value>
    - ${name} ==> ${value}<#lt>
</#list>

10) Accessing Documents
---------------------------------------------------------------------------
Get the number of documents:
- ${Documents.size()}
<#if !Documents.isEmpty()>
    Get the first document
    - ${Documents.get(0)!"NA"}
</#if>
List all files containing "README" in the name
<#list Documents.find("*README*") as document>
    - ${document.name}
</#list>
List all files having "md" extension
<#list Documents.find("*.md") as document>
    - ${document.name}
</#list>
Get all documents
<#list Documents.list as document>
    - ${document.name} => ${document.location}
</#list>

11) Document Data Model
---------------------------------------------------------------------------
Top-level entries in the current data model
<#list .data_model?keys?sort as key>
    - ${key}<#lt>
</#list>

12) Create a UUID
---------------------------------------------------------------------------
See https://stackoverflow.com/questions/43501297/i-have-a-simplescalar-i-need-its-strings-getbytes-return-value-what-can-i-d
<#assign uuidSource = "value and salt">
<#assign buffer = FreeMarkerTool.statics["java.nio.charset.Charset"].forName("UTF-8").encode(uuidSource).rewind()>
<#assign bytes = buffer.array()[0..<buffer.limit()]>
<#assign uuid = FreeMarkerTool.statics["java.util.UUID"].nameUUIDFromBytes(bytes)>
Random UUID           : ${FreeMarkerTool.statics["java.util.UUID"].randomUUID()}
Name UUID from bytes  : ${uuid}
Name UUID as function : ${uuidFromValueAndSalt("value and ", "salt")}

13) Printing Special Characters
---------------------------------------------------------------------------
German Special Characters: äöüßÄÖÜ

14) Locale-specific output
---------------------------------------------------------------------------
<#setting number_format=",##0.00">
<#assign smallNumber = 1.234>
<#assign largeNumber = 12345678.9>
Small Number :  ${smallNumber}
Large Number :  ${largeNumber}
Currency     :  ${largeNumber} EUR
Date         :  ${.now?date}
Time         :  ${.now?time}

15) Execute a program
---------------------------------------------------------------------------
> date
${ExecTool.execute("date")}

<#--------------------------------------------------------------------------->
<#function uuidFromValueAndSalt value salt>
    <#assign uuidSource = value + salt>
    <#assign buffer = FreeMarkerTool.statics["java.nio.charset.Charset"].forName("UTF-8").encode(uuidSource).rewind()>
    <#assign bytes = buffer.array()[0..<buffer.limit()]>
    <#return FreeMarkerTool.statics["java.util.UUID"].nameUUIDFromBytes(bytes)>
</#function>
```

# 7. Design Considerations

## 7.1 How It Works

* The user-supplied files are loaded or input is read from `stdin`
* The FreeMarker data model containing the documents and tools is created and passed to the template
* The generated output is written to the user-supplied file or to `stdout`

## 7.2 FreeMarker Data Model

Within the script a FreeMarker data model is set up and passed to the template - it contains the documents to be processed and the following tools

| Helper                | Description                                                         |
|-----------------------|---------------------------------------------------------------------|
| CSVTool               | CSV parser exposing a `parse` method                                |
| ExecTool              | Executing commons using Apache Commons Exec                         |
| Documents             | Helper to find documents, e.g. by name, extension or index          |
| ExcelTool             | Excel parser exposing a `parse` method                              |
| FreeMarkerTool        | FreeMarker helper classes                                           |
| GrokTool              | Use Grok for powerful regular expressions                           |
| JsonPathTool          | JSON Parser                                                         |
| JsoupTool             | Jsoup HTML parser                                                   |
| PropertiesTool        | Properties parser exposing a `parse` method                         |
| XmlTool               | XML parser exposing a `parse` method                                |
| YamlTool              | SnakeYAML to parse YAML files                                       |

# 8. Tips & Tricks

## 8.1 Template Base Directory

When doing some ad-hoc scripting it is useful to rely on a base directory to resolve the FTL templates

* As a default the FTL templates are resolved relative to the script directory
* The caller can provide a `-b` or `--basedir` command line parameter

> ./bin/freemarker-cli -t templates/json/html/customer-user-products.ftl freemarker-cli/site/sample/json/customer-user-products.jso

## 8.2 Using Pipes

When doing ad-hoc scripting it useful to pipe the output of one command directly into "freemarker-cli"

> cat site/sample/json/customer-user-products.json | ./bin/freemarker-cli -t ./templates/json/html/customer-user-products.ftl --stdin