import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Subject from './subject';
import SubjectDetail from './subject-detail';
import SubjectUpdate from './subject-update';
import SubjectDeleteDialog from './subject-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubjectUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubjectUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubjectDetail} />
      <ErrorBoundaryRoute path={match.url} component={Subject} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SubjectDeleteDialog} />
  </>
);

export default Routes;
