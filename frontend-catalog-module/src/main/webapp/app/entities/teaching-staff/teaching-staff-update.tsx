import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPaper } from 'app/shared/model/paper.model';
import { getEntities as getPapers } from 'app/entities/paper/paper.reducer';
import { getEntity, updateEntity, createEntity, reset } from './teaching-staff.reducer';
import { ITeachingStaff } from 'app/shared/model/teaching-staff.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITeachingStaffUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITeachingStaffUpdateState {
  isNew: boolean;
  idspaper: any[];
}

export class TeachingStaffUpdate extends React.Component<ITeachingStaffUpdateProps, ITeachingStaffUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idspaper: [],
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

    this.props.getPapers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { teachingStaffEntity } = this.props;
      const entity = {
        ...teachingStaffEntity,
        ...values,
        papers: mapIdList(values.papers)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/teaching-staff');
  };

  render() {
    const { teachingStaffEntity, papers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="catalogApp.teachingStaff.home.createOrEditLabel">
              <Translate contentKey="catalogApp.teachingStaff.home.createOrEditLabel">Create or edit a TeachingStaff</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : teachingStaffEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="teaching-staff-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="teaching-staff-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guidLabel" for="teaching-staff-guid">
                    <Translate contentKey="catalogApp.teachingStaff.guid">Guid</Translate>
                  </Label>
                  <AvField
                    id="teaching-staff-guid"
                    type="text"
                    name="guid"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="teaching-staff-name">
                    <Translate contentKey="catalogApp.teachingStaff.name">Name</Translate>
                  </Label>
                  <AvField id="teaching-staff-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="graduationTypeLabel" for="teaching-staff-graduationType">
                    <Translate contentKey="catalogApp.teachingStaff.graduationType">Graduation Type</Translate>
                  </Label>
                  <AvInput
                    id="teaching-staff-graduationType"
                    type="select"
                    className="form-control"
                    name="graduationType"
                    value={(!isNew && teachingStaffEntity.graduationType) || 'POST_GRADUATE'}
                  >
                    <option value="POST_GRADUATE">{translate('catalogApp.GraduationType.POST_GRADUATE')}</option>
                    <option value="MASTER_DEGREE">{translate('catalogApp.GraduationType.MASTER_DEGREE')}</option>
                    <option value="DOCTOR_DEGREE">{translate('catalogApp.GraduationType.DOCTOR_DEGREE')}</option>
                    <option value="PHD">{translate('catalogApp.GraduationType.PHD')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="teaching-staff-paper">
                    <Translate contentKey="catalogApp.teachingStaff.paper">Paper</Translate>
                  </Label>
                  <AvInput
                    id="teaching-staff-paper"
                    type="select"
                    multiple
                    className="form-control"
                    name="papers"
                    value={teachingStaffEntity.papers && teachingStaffEntity.papers.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {papers
                      ? papers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/teaching-staff" replace color="info">
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
  papers: storeState.paper.entities,
  teachingStaffEntity: storeState.teachingStaff.entity,
  loading: storeState.teachingStaff.loading,
  updating: storeState.teachingStaff.updating,
  updateSuccess: storeState.teachingStaff.updateSuccess
});

const mapDispatchToProps = {
  getPapers,
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
)(TeachingStaffUpdate);
