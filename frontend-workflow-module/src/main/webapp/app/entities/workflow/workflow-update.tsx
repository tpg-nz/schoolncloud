import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getWorkflows } from 'app/entities/workflow/workflow.reducer';
import { getEntity, updateEntity, createEntity, reset } from './workflow.reducer';
import { IWorkflow } from 'app/shared/model/workflow.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IWorkflowUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IWorkflowUpdateState {
  isNew: boolean;
  versionOfId: string;
}

export class WorkflowUpdate extends React.Component<IWorkflowUpdateProps, IWorkflowUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      versionOfId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getWorkflows();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { workflowEntity } = this.props;
      const entity = {
        ...workflowEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/workflow');
  };

  render() {
    const { workflowEntity, workflows, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="workflowApp.workflow.home.createOrEditLabel">
              <Translate contentKey="workflowApp.workflow.home.createOrEditLabel">Create or edit a Workflow</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : workflowEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="workflow-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="workflow-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="workflow-name">
                    <Translate contentKey="workflowApp.workflow.name">Name</Translate>
                  </Label>
                  <AvField
                    id="workflow-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 40, errorMessage: translate('entity.validation.maxlength', { max: 40 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="workflow-description">
                    <Translate contentKey="workflowApp.workflow.description">Description</Translate>
                  </Label>
                  <AvField id="workflow-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="versionLabel" for="workflow-version">
                    <Translate contentKey="workflowApp.workflow.version">Version</Translate>
                  </Label>
                  <AvField
                    id="workflow-version"
                    type="text"
                    name="version"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="enabledLabel" check>
                    <AvInput id="workflow-enabled" type="checkbox" className="form-control" name="enabled" />
                    <Translate contentKey="workflowApp.workflow.enabled">Enabled</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="workflow-versionOf">
                    <Translate contentKey="workflowApp.workflow.versionOf">Version Of</Translate>
                  </Label>
                  <AvInput id="workflow-versionOf" type="select" className="form-control" name="versionOf.id">
                    <option value="" key="0" />
                    {workflows
                      ? workflows.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/workflow" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  workflows: storeState.workflow.entities,
  workflowEntity: storeState.workflow.entity,
  loading: storeState.workflow.loading,
  updating: storeState.workflow.updating,
  updateSuccess: storeState.workflow.updateSuccess
});

const mapDispatchToProps = {
  getWorkflows,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorkflowUpdate);
