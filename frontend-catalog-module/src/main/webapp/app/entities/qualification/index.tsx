import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Qualification from './qualification';
import QualificationDetail from './qualification-detail';
import QualificationUpdate from './qualification-update';
import QualificationDeleteDialog from './qualification-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={QualificationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={QualificationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={QualificationDetail} />
      <ErrorBoundaryRoute path={match.url} component={Qualification} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={QualificationDeleteDialog} />
  </>
);

export default Routes;
