##Configuration for Heroku

Commits to master are immediately deployed by our heroku hook [here].

To configure the application to work with a heroku account of your own from a local branch:

1.) Install the heroku toolbelt: `wget -O- https://toolbelt.heroku.com/install-ubuntu.sh | sh`

2.) Log in to the heroku cli: `heroku login`

3.) Navigate to your local repository and check out the branch you want to deploy

4.) Create a heroku remote: `heroku create`

5.) Make a commit to your local branch

6.) Set up the heroku spring profile environment variable: `heroku config:set spring.profiles.active=heroku`

7.) Push your branch to the heroku remote: `git push heroku {your-branch-name}:master`

8.) Once the app has successfully installed, scale heroku: `heroku ps:scale web=1`

9.) Check out heroku's logs to see the Tomcat container's startup progress: `heroku logs`

10.) Once the container is up, check it out in a browser: `heroku open`

Note: It could take a while for the container to start, given the current memory requirements of the app. The logs
might show a series of Error R14: (Memory quota exceeded) for this reason. Don't worry, the application will still
deploy, but heroku is paging memory to disk and will be quite slow.

[here]: https://mysterious-everglades-5022.herokuapp.com
