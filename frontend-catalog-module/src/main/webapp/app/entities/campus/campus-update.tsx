import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IEducationalInstituition } from 'app/shared/model/educational-instituition.model';
import { getEntities as getEducationalInstituitions } from 'app/entities/educational-instituition/educational-instituition.reducer';
import { getEntity, updateEntity, createEntity, reset } from './campus.reducer';
import { ICampus } from 'app/shared/model/campus.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICampusUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICampusUpdateState {
  isNew: boolean;
  educationalInstitutionId: string;
}

export class CampusUpdate extends React.Component<ICampusUpdateProps, ICampusUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      educationalInstitutionId: '0',
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

    this.props.getEducationalInstituitions();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { campusEntity } = this.props;
      const entity = {
        ...campusEntity,
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
    this.props.history.push('/entity/campus');
  };

  render() {
    const { campusEntity, educationalInstituitions, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="catalogApp.campus.home.createOrEditLabel">
              <Translate contentKey="catalogApp.campus.home.createOrEditLabel">Create or edit a Campus</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : campusEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="campus-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="campus-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guidLabel" for="campus-guid">
                    <Translate contentKey="catalogApp.campus.guid">Guid</Translate>
                  </Label>
                  <AvField
                    id="campus-guid"
                    type="text"
                    name="guid"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="campus-name">
                    <Translate contentKey="catalogApp.campus.name">Name</Translate>
                  </Label>
                  <AvField id="campus-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label for="campus-educationalInstitution">
                    <Translate contentKey="catalogApp.campus.educationalInstitution">Educational Institution</Translate>
                  </Label>
                  <AvInput
                    id="campus-educationalInstitution"
                    type="select"
                    className="form-control"
                    name="educationalInstitution.id"
                    value={isNew ? educationalInstituitions[0] && educationalInstituitions[0].id : campusEntity.educationalInstitution.id}
                    required
                  >
                    {educationalInstituitions
                      ? educationalInstituitions.map(otherEntity => (
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
                <Button tag={Link} id="cancel-save" to="/entity/campus" replace color="info">
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
  educationalInstituitions: storeState.educationalInstituition.entities,
  campusEntity: storeState.campus.entity,
  loading: storeState.campus.loading,
  updating: storeState.campus.updating,
  updateSuccess: storeState.campus.updateSuccess
});

const mapDispatchToProps = {
  getEducationalInstituitions,
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
)(CampusUpdate);
