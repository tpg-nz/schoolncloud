import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Paper from './paper';
import PaperDetail from './paper-detail';
import PaperUpdate from './paper-update';
import PaperDeleteDialog from './paper-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PaperUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PaperUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PaperDetail} />
      <ErrorBoundaryRoute path={match.url} component={Paper} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PaperDeleteDialog} />
  </>
);

export default Routes;
