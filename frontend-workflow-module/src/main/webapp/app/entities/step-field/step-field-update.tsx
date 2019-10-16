import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IStep } from 'app/shared/model/step.model';
import { getEntities as getSteps } from 'app/entities/step/step.reducer';
import { getEntity, updateEntity, createEntity, reset } from './step-field.reducer';
import { IStepField } from 'app/shared/model/step-field.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IStepFieldUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IStepFieldUpdateState {
  isNew: boolean;
  stepId: string;
}

export class StepFieldUpdate extends React.Component<IStepFieldUpdateProps, IStepFieldUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      stepId: '0',
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

    this.props.getSteps();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { stepFieldEntity } = this.props;
      const entity = {
        ...stepFieldEntity,
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
    this.props.history.push('/entity/step-field');
  };

  render() {
    const { stepFieldEntity, steps, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="workflowApp.stepField.home.createOrEditLabel">
              <Translate contentKey="workflowApp.stepField.home.createOrEditLabel">Create or edit a StepField</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {(isNew && loading) || (!isNew && (loading || stepFieldEntity.step === undefined)) ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : stepFieldEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="step-field-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="step-field-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="sequenceLabel" for="step-field-sequence">
                    <Translate contentKey="workflowApp.stepField.sequence">Sequence</Translate>
                  </Label>
                  <AvField
                    id="step-field-sequence"
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
                  <Label id="labelLabel" for="step-field-label">
                    <Translate contentKey="workflowApp.stepField.label">Label</Translate>
                  </Label>
                  <AvField
                    id="step-field-label"
                    type="text"
                    name="label"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="fieldTypeLabel" for="step-field-fieldType">
                    <Translate contentKey="workflowApp.stepField.fieldType">Field Type</Translate>
                  </Label>
                  <AvInput
                    id="step-field-fieldType"
                    type="select"
                    className="form-control"
                    name="fieldType"
                    value={(!isNew && stepFieldEntity.fieldType) || 'TEXT_FIELD'}
                  >
                    <option value="TEXT_FIELD">{translate('workflowApp.FieldType.TEXT_FIELD')}</option>
                    <option value="TEXT_AREA">{translate('workflowApp.FieldType.TEXT_AREA')}</option>
                    <option value="COMBOBOX">{translate('workflowApp.FieldType.COMBOBOX')}</option>
                    <option value="CHECKBOX">{translate('workflowApp.FieldType.CHECKBOX')}</option>
                    <option value="RADIOBOX">{translate('workflowApp.FieldType.RADIOBOX')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="step-field-step">
                    <Translate contentKey="workflowApp.stepField.step">Step</Translate>
                  </Label>
                  <AvInput
                    id="step-field-step"
                    type="select"
                    className="form-control"
                    name="step.id"
                    value={isNew ? steps[0] && steps[0].id : stepFieldEntity.step.id}
                    required
                  >
                    {steps
                      ? steps.map(otherEntity => (
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
                <Button tag={Link} id="cancel-save" to="/entity/step-field" replace color="info">
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
  steps: storeState.step.entities,
  stepFieldEntity: storeState.stepField.entity,
  loading: storeState.stepField.loading,
  updating: storeState.stepField.updating,
  updateSuccess: storeState.stepField.updateSuccess
});

const mapDispatchToProps = {
  getSteps,
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
)(StepFieldUpdate);
