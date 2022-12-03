# Lawnlayer Game

The player uses arrow keys to move on the map and tries to avoid enemies (worms and beetles) that are bouncing around the map while capturing the area. Once a percentage of the map area has been captured, the level is won.

There are two power-ups (cheese): slowing the enemies and speeding up the player for 10 seconds.

If the player circles back and hits their own in-progress path, they lose a life and respawn.
The player loses a life and respawns when:
* they circle back and hit their own in-progress path.
* the red path hits the player before they reach a safe tile (an enemy hits the path the player is laying out, the path tiles it hits will turn red and propagate outward towards the player).

![image](https://user-images.githubusercontent.com/60818058/205441302-e0c42b42-d1ab-4083-915e-c7ca77017add.png)

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
