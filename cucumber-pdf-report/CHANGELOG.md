# Changelog
All notable changes to this project will be documented in this file.

## 2.6.0 (2022-4-10)
* Split lengthy feature & scenario names correctly
* Restricted tags display length

## 2.5.0 (2022-3-4)
* Removed duplicate code which is present in table-layout dependency
* File attachment option added for executables

## 2.4.0 (2022-3-2)
* Refactored for Rest Assured pdf report

## 2.3.0 (2021-8-3)
* Display html table content in tabular format [Issue 31](https://github.com/grasshopper7/cucumber-pdf-report/issues/31)

## 2.2.4 (2021-8-3)
* Delete temporary images created for base64 strings [Issue 30](https://github.com/grasshopper7/cucumber-pdf-report/issues/30)

## 2.2.3 (2021-6-12)
* Additional check in case of no feature executed [Issue 29](https://github.com/grasshopper7/cucumber-pdf-report/issues/29)

## 2.2.2 (2021-6-11)
* Fix errors when detailed section is set to false [Issue 28](https://github.com/grasshopper7/cucumber-pdf-report/issues/28)
* Fix checkdata function not called [Issue 29](https://github.com/grasshopper7/cucumber-pdf-report/issues/29)

## 2.2.1 (2021-4-19)
* Fix expanded section display logic [Issue 26](https://github.com/grasshopper7/cucumber-pdf-report/issues/26)
* Reversed keyword display issue [Issue 11](https://github.com/grasshopper7/cucumber-pdf-report/issues/11)

## 2.2.0 (2021-4-16)
* Split details like stacks and logs and docstrings over multiple pages to save space [Issue 23](https://github.com/grasshopper7/cucumber-pdf-report/issues/23)
* Display large sized images in separate section [Issue 24](https://github.com/grasshopper7/cucumber-pdf-report/issues/24)
* Skip status message display [Issue 22](https://github.com/grasshopper7/cucumber-pdf-report/issues/22)

## 2.1.0 (2021-3-9)
* Stop report creation if no features executed [Issue 21](https://github.com/grasshopper7/cucumber-pdf-report/issues/21)

## 2.0.0 (2021-3-7)
* Switch to landscape page for report [Issue 19](https://github.com/grasshopper7/cucumber-pdf-report/issues/19)
* Refactor dashboard page [Issue 14](https://github.com/grasshopper7/cucumber-pdf-report/issues/14)
* Refactor Feature & Scenario pages [Issue 15](https://github.com/grasshopper7/cucumber-pdf-report/issues/15)
* Refactor detailed feature and scenario section [Issue 16](https://github.com/grasshopper7/cucumber-pdf-report/issues/16)
* Refactor stacktrace and logs display [Issue 17](https://github.com/grasshopper7/cucumber-pdf-report/issues/17)
* Add docstring and datatable support [Issue 2](https://github.com/grasshopper7/cucumber-pdf-report/issues/2)
* Add support for media [Issue 7](https://github.com/grasshopper7/cucumber-pdf-report/issues/7)
* Refactor report configurations [Issue 20](https://github.com/grasshopper7/cucumber-pdf-report/issues/20)
* Keywords missing in PDF [Issue 11](https://github.com/grasshopper7/cucumber-pdf-report/issues/11)
* Lines do not wrap in PDF [Issue 12](https://github.com/grasshopper7/cucumber-pdf-report/issues/12)

## 1.7.0 (2020-12-13)

* Handled special characters by using Liberation font [Issue 9](https://github.com/grasshopper7/cucumber-pdf-report/issues/9)

## 1.6.0 (2020-11-18)

* Reduce number of exception stacktrace lines to 10 [Issue 8](https://github.com/grasshopper7/cucumber-pdf-report/issues/8)

## 1.5.0 (2020-11-3)

* Refactored time duration and default report name

## 1.4.0 (2020-10-24)

* Capability to add step hooks individually to step [Issue 5](https://github.com/grasshopper7/cucumber-pdf-report/issues/5)
* Add millisecond to datetime for start and end times [Issue 6](https://github.com/grasshopper7/cucumber-pdf-report/issues/6)


## 1.3.0 (2020-10-21)

* Split extent reporter into separate jar [Issue 4](https://github.com/grasshopper7/cucumber-pdf-report/issues/4)


## 1.2.0 (2020-10-20)

* Remove invalid characters in log and error messages + upper case hook type [Issue 3](https://github.com/grasshopper7/cucumber-pdf-report/issues/3)


## 1.1.0 (2020-10-20)

* Option to indicate when media is attached in step or hook [Issue 1](https://github.com/grasshopper7/cucumber-pdf-report/issues/1)


## 1.0.0 (2020-10-17)

* Initial release
