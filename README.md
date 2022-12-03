# Lawnlayer Game

The player uses arrow keys to move on the map and tries to avoid enemies that are bouncing around the map while capturing the area. Once a percentage of the map area has been captured, the level is won.

There are two power-ups: slowing the enemies and speeding up the player for 10 seconds.

## Usage
The app can be downloaded by cloning the project.
```
git clone https://github.com/hnguyen111/Lawnlayer.git
```

At the root directory of the project folder "app", run the following commands:
```
gradle clean build
gradle run
```

To run all the test cases, run:
```
gradle test
```

To generate the testing code coverage report with gradle using jacoco, run:
```
gradle test jacocoTestReport
```

JaCoCo report can be accessed locally from:
app/build/reports/jacoco/test/html/index.html

## Contributing
To make contributions to the codebase, you can fork this repository first, and clone your forked repository to your local machine. You then can create your own branch and make changes to it. After finishing your work, push your branch to GitHub and open a pull request for review.

You have to add test cases as part of your changes.

## Code Contributor

Hantha Nguyen

## License
[MIT](https://choosealicense.com/licenses/mit/)
