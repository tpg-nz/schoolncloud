import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subject.reducer';
import { ISubject } from 'app/shared/model/subject.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubjectProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Subject extends React.Component<ISubjectProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { subjectList, match } = this.props;
    return (
      <div>
        <h2 id="subject-heading">
          <Translate contentKey="catalogApp.subject.home.title">Subjects</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="catalogApp.subject.home.createLabel">Create new Subject</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {subjectList && subjectList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.subject.guid">Guid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.subject.name">Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.subject.overview">Overview</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.subject.level">Level</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {subjectList.map((subject, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${subject.id}`} color="link" size="sm">
                        {subject.id}
                      </Button>
                    </td>
                    <td>{subject.guid}</td>
                    <td>{subject.name}</td>
                    <td>{subject.overview}</td>
                    <td>{subject.level}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${subject.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${subject.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${subject.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="catalogApp.subject.home.notFound">No Subjects found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ subject }: IRootState) => ({
  subjectList: subject.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Subject);
