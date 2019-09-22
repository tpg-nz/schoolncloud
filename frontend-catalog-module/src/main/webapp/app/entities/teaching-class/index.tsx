import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TeachingClass from './teaching-class';
import TeachingClassDetail from './teaching-class-detail';
import TeachingClassUpdate from './teaching-class-update';
import TeachingClassDeleteDialog from './teaching-class-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TeachingClassUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TeachingClassUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TeachingClassDetail} />
      <ErrorBoundaryRoute path={match.url} component={TeachingClass} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TeachingClassDeleteDialog} />
  </>
);

export default Routes;
