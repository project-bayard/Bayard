### Bayard Development Workflow

Our development process casually follows the [Github Flow]. Want to contribute? Open a pull request against
our staging branch! We've certainly [got issues].

When contributing there might be cases where you don't want to trigger a travis build (maybe you're editing a README.md).
Just include [ci skip] anywhere in your commit message. For example:

`git commit -a -m "Made superfluous changes to a .md file please dont test me! [ci skip]"`

If it's part of a pull request, just make sure that the last commit of the pull contains the [ci skip].

It's also a breeze to deploy the app on Heroku while you develop. [Here's an overview of the Heroku configuration.]

Drop the dev team a line if you have any questions, want to contribute and don't know where to start,
or just want to say hi: projectbayardsoftware@gmail.com

[Github Flow]: https://guides.github.com/introduction/flow/
[got issues]: https://github.com/project-bayard/Bayard/issues
[Here's an overview of the Heroku configuration.]: https://github.com/project-bayard/Bayard/blob/master/heroku.md