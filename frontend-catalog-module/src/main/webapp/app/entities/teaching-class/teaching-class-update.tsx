import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICampus } from 'app/shared/model/campus.model';
import { getEntities as getCampuses } from 'app/entities/campus/campus.reducer';
import { IPaper } from 'app/shared/model/paper.model';
import { getEntities as getPapers } from 'app/entities/paper/paper.reducer';
import { getEntity, updateEntity, createEntity, reset } from './teaching-class.reducer';
import { ITeachingClass } from 'app/shared/model/teaching-class.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITeachingClassUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITeachingClassUpdateState {
  isNew: boolean;
  campusId: string;
  paperId: string;
}

export class TeachingClassUpdate extends React.Component<ITeachingClassUpdateProps, ITeachingClassUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      campusId: '0',
      paperId: '0',
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

    this.props.getCampuses();
    this.props.getPapers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { teachingClassEntity } = this.props;
      const entity = {
        ...teachingClassEntity,
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
    this.props.history.push('/entity/teaching-class');
  };

  render() {
    const { teachingClassEntity, campuses, papers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="catalogApp.teachingClass.home.createOrEditLabel">
              <Translate contentKey="catalogApp.teachingClass.home.createOrEditLabel">Create or edit a TeachingClass</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : teachingClassEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="teaching-class-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="teaching-class-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="codeLabel" for="teaching-class-code">
                    <Translate contentKey="catalogApp.teachingClass.code">Code</Translate>
                  </Label>
                  <AvField
                    id="teaching-class-code"
                    type="text"
                    name="code"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="yearLabel" for="teaching-class-year">
                    <Translate contentKey="catalogApp.teachingClass.year">Year</Translate>
                  </Label>
                  <AvField
                    id="teaching-class-year"
                    type="string"
                    className="form-control"
                    name="year"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="semesterLabel" for="teaching-class-semester">
                    <Translate contentKey="catalogApp.teachingClass.semester">Semester</Translate>
                  </Label>
                  <AvField
                    id="teaching-class-semester"
                    type="string"
                    className="form-control"
                    name="semester"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="teaching-class-campus">
                    <Translate contentKey="catalogApp.teachingClass.campus">Campus</Translate>
                  </Label>
                  <AvInput
                    id="teaching-class-campus"
                    type="select"
                    className="form-control"
                    name="campus.id"
                    value={isNew ? campuses[0] && campuses[0].id : teachingClassEntity.campus.id}
                    required
                  >
                    {campuses
                      ? campuses.map(otherEntity => (
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
                <AvGroup>
                  <Label for="teaching-class-paper">
                    <Translate contentKey="catalogApp.teachingClass.paper">Paper</Translate>
                  </Label>
                  <AvInput
                    id="teaching-class-paper"
                    type="select"
                    className="form-control"
                    name="paper.id"
                    value={isNew ? papers[0] && papers[0].id : teachingClassEntity.paper.id}
                    required
                  >
                    {papers
                      ? papers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.code}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvFeedback>
                    <Translate contentKey="entity.validation.required">This field is required.</Translate>
                  </AvFeedback>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/teaching-class" replace color="info">
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
  campuses: storeState.campus.entities,
  papers: storeState.paper.entities,
  teachingClassEntity: storeState.teachingClass.entity,
  loading: storeState.teachingClass.loading,
  updating: storeState.teachingClass.updating,
  updateSuccess: storeState.teachingClass.updateSuccess
});

const mapDispatchToProps = {
  getCampuses,
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
)(TeachingClassUpdate);
