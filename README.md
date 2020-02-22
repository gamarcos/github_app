# Room + RxJava + Android architecture components

This is a project that uses observables in Room, with RxJava's Single`s objects. Based on Android architecture components

## Introduction


### Functionality

 - Shows a public gists
 - Save a list of lists locally
 - Favorite gists locally
 - List the gist file type
 - Show detail owner gist
    - name
    - photo
 - Search for owner name (TODO)
    
### Project Architecture

The project follows the concept of Android architecture components:

![Image of Android architecture components](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

### Product Flavors

To use the concept of productFlavors two flavors with different colors were added to represent two applications with the same codebase.

 - githubblue
 - githubgreen
 
 ### Technologies
The following strategies were used for local storage, data connection, asynchronous calls and dependency injection.

 - RxAndroid
 - Retrofit
 - Dagger2
 - Room
 
### API
https://developer.github.com/v3/

### Tests
 - GistMapperTest
 - GistRepositoryImplTest
 - GistMapperTest
 - GistViewModelTest
 - In Progress
 
 ### Improvements
  - Rx Android (Urgent)

