# Codecoach


## Deployment

The frontend and the backend are deployed independently from eachother.

We use two environments: staging and production. 

The staging environment will reflect the latest state of the development whereas the
production environment tries to only contain "finished" features.

STAGING endpoints:
- https://codecoach-staging.netlify.app/home (frontend)
- https://codecoach-staging.herokuapp.com/ (backend)

PRODUCTION endpoints:
- https://codecoach.netlify.app/home (frontend)
- https://codecoach-prod.herokuapp.com/ (backend)

### Backend

The production and staging backend server that runs on heroku is deployed
through github actions.

To deploy to staging, simply push a commit to the main branch on Github.
A Github Action will perform unit tests, and when successful, will
deploy the revision to heroku.

To deploy to production, a similar github action is used, but you need
to manually trigger it. To do so, go to "Actions" tab, then on your left
click on the "Deploy Production" workflow, and finally click through the
"Run Workflow" dialog.

### Frontend

Netlify is configured to monitor the production and staging branch of the
frontend repository. Whenever a new commit is pushed to one of those branches,
netlify will deploy to the respective environment.

Initially we deployed every commit on the main branch to the staging environment
similar to the backend, but netlify enforces a time-limit on their build server
that we would exceed within days if we kept deploying on every push.

In order to deploy, use a pull request do merge from the main branch onto the
staging branch, and a merge from the main branch (or even better, the staging
branch) onto the production branch.
