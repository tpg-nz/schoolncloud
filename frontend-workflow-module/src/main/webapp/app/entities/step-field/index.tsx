import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StepField from './step-field';
import StepFieldDetail from './step-field-detail';
import StepFieldUpdate from './step-field-update';
import StepFieldDeleteDialog from './step-field-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StepFieldUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StepFieldUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StepFieldDetail} />
      <ErrorBoundaryRoute path={match.url} component={StepField} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={StepFieldDeleteDialog} />
  </>
);

export default Routes;
