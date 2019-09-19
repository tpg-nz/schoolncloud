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
import { IPaper } from 'app/shared/model/paper.model';
import { getEntities as getPapers } from 'app/entities/paper/paper.reducer';
import { getEntity, updateEntity, createEntity, reset } from './requirement.reducer';
import { IRequirement } from 'app/shared/model/requirement.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRequirementUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRequirementUpdateState {
  isNew: boolean;
  subjectId: string;
  paperId: string;
}

export class RequirementUpdate extends React.Component<IRequirementUpdateProps, IRequirementUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      subjectId: '0',
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

    this.props.getSubjects();
    this.props.getPapers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { requirementEntity } = this.props;
      const entity = {
        ...requirementEntity,
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
    this.props.history.push('/entity/requirement');
  };

  render() {
    const { requirementEntity, subjects, papers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="catalogApp.requirement.home.createOrEditLabel">
              <Translate contentKey="catalogApp.requirement.home.createOrEditLabel">Create or edit a Requirement</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : requirementEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="requirement-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="requirement-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="levelLabel" for="requirement-level">
                    <Translate contentKey="catalogApp.requirement.level">Level</Translate>
                  </Label>
                  <AvField id="requirement-level" type="string" className="form-control" name="level" />
                </AvGroup>
                <AvGroup>
                  <Label for="requirement-subject">
                    <Translate contentKey="catalogApp.requirement.subject">Subject</Translate>
                  </Label>
                  <AvInput
                    id="requirement-subject"
                    type="select"
                    className="form-control"
                    name="subject.id"
                    value={isNew ? subjects[0] && subjects[0].id : requirementEntity.subject.id}
                    required
                  >
                    {subjects
                      ? subjects.map(otherEntity => (
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
                  <Label for="requirement-paper">
                    <Translate contentKey="catalogApp.requirement.paper">Paper</Translate>
                  </Label>
                  <AvInput
                    id="requirement-paper"
                    type="select"
                    className="form-control"
                    name="paper.id"
                    value={isNew ? papers[0] && papers[0].id : requirementEntity.paper.id}
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
                <Button tag={Link} id="cancel-save" to="/entity/requirement" replace color="info">
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
  papers: storeState.paper.entities,
  requirementEntity: storeState.requirement.entity,
  loading: storeState.requirement.loading,
  updating: storeState.requirement.updating,
  updateSuccess: storeState.requirement.updateSuccess
});

const mapDispatchToProps = {
  getSubjects,
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
)(RequirementUpdate);
