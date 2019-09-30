import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IWorkflow } from 'app/shared/model/workflow.model';
import { getEntities as getWorkflows } from 'app/entities/workflow/workflow.reducer';
import { getEntity, updateEntity, createEntity, reset } from './step.reducer';
import { IStep } from 'app/shared/model/step.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IStepUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IStepUpdateState {
  isNew: boolean;
  workflowId: string;
}

export class StepUpdate extends React.Component<IStepUpdateProps, IStepUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      workflowId: '0',
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
      const { stepEntity } = this.props;
      const entity = {
        ...stepEntity,
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
    this.props.history.push('/entity/step');
  };

  render() {
    const { stepEntity, workflows, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="workflowApp.step.home.createOrEditLabel">
              <Translate contentKey="workflowApp.step.home.createOrEditLabel">Create or edit a Step</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {(isNew && loading) || (!isNew && (loading || stepEntity.workflow === undefined)) ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : stepEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="step-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="step-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="sequenceLabel" for="step-sequence">
                    <Translate contentKey="workflowApp.step.sequence">Sequence</Translate>
                  </Label>
                  <AvField
                    id="step-sequence"
                    type="string"
                    className="form-control"
                    name="sequence"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 1, errorMessage: translate('entity.validation.min', { min: 1 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="step-name">
                    <Translate contentKey="workflowApp.step.name">Name</Translate>
                  </Label>
                  <AvField
                    id="step-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="step-workflow">
                    <Translate contentKey="workflowApp.step.workflow">Workflow</Translate>
                  </Label>
                  <AvInput
                    id="step-workflow"
                    type="select"
                    className="form-control"
                    name="workflow.id"
                    value={isNew ? workflows[0] && workflows[0].id : stepEntity.workflow.id}
                    required
                  >
                    {workflows
                      ? workflows.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvFeedback>
                    <Translate contentKey="entity.validation.required">This field is required.</Translate>
                  </AvFeedback>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/step" replace color="info">
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
  stepEntity: storeState.step.entity,
  loading: storeState.step.loading,
  updating: storeState.step.updating,
  updateSuccess: storeState.step.updateSuccess
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
)(StepUpdate);
