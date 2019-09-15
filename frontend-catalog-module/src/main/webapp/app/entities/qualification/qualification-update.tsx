import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISubject } from 'app/shared/model/subject.model';
import { getEntities as getSubjects } from 'app/entities/subject/subject.reducer';
import { getEntity, updateEntity, createEntity, reset } from './qualification.reducer';
import { IQualification } from 'app/shared/model/qualification.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IQualificationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IQualificationUpdateState {
  isNew: boolean;
  subjectId: string;
}

export class QualificationUpdate extends React.Component<IQualificationUpdateProps, IQualificationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      subjectId: '0',
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

    this.props.getSubjects();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { qualificationEntity } = this.props;
      const entity = {
        ...qualificationEntity,
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
    this.props.history.push('/entity/qualification');
  };

  render() {
    const { qualificationEntity, subjects, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="catalogApp.qualification.home.createOrEditLabel">
              <Translate contentKey="catalogApp.qualification.home.createOrEditLabel">Create or edit a Qualification</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : qualificationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="qualification-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="qualification-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guidLabel" for="qualification-guid">
                    <Translate contentKey="catalogApp.qualification.guid">Guid</Translate>
                  </Label>
                  <AvField
                    id="qualification-guid"
                    type="text"
                    name="guid"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="qualification-name">
                    <Translate contentKey="catalogApp.qualification.name">Name</Translate>
                  </Label>
                  <AvField id="qualification-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="hyperLinkLabel" for="qualification-hyperLink">
                    <Translate contentKey="catalogApp.qualification.hyperLink">Hyper Link</Translate>
                  </Label>
                  <AvField id="qualification-hyperLink" type="text" name="hyperLink" />
                </AvGroup>
                <AvGroup>
                  <Label for="qualification-subject">
                    <Translate contentKey="catalogApp.qualification.subject">Subject</Translate>
                  </Label>
                  <AvInput id="qualification-subject" type="select" className="form-control" name="subject.id">
                    <option value="" key="0" />
                    {subjects
                      ? subjects.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.guid}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/qualification" replace color="info">
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
  subjects: storeState.subject.entities,
  qualificationEntity: storeState.qualification.entity,
  loading: storeState.qualification.loading,
  updating: storeState.qualification.updating,
  updateSuccess: storeState.qualification.updateSuccess
});

const mapDispatchToProps = {
  getSubjects,
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
)(QualificationUpdate);
