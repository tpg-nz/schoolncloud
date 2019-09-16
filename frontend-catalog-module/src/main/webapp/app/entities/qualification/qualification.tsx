import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './qualification.reducer';
import { IQualification } from 'app/shared/model/qualification.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IQualificationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Qualification extends React.Component<IQualificationProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { qualificationList, match } = this.props;
    return (
      <div>
        <h2 id="qualification-heading">
          <Translate contentKey="catalogApp.qualification.home.title">Qualifications</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="catalogApp.qualification.home.createLabel">Create new Qualification</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {qualificationList && qualificationList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.qualification.guid">Guid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.qualification.name">Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.qualification.hyperLink">Hyper Link</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.qualification.subject">Subject</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {qualificationList.map((qualification, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${qualification.id}`} color="link" size="sm">
                        {qualification.id}
                      </Button>
                    </td>
                    <td>{qualification.guid}</td>
                    <td>{qualification.name}</td>
                    <td>{qualification.hyperLink}</td>
                    <td>
                      {qualification.subject ? <Link to={`subject/${qualification.subject.id}`}>{qualification.subject.guid}</Link> : ''}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${qualification.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${qualification.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${qualification.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="catalogApp.qualification.home.notFound">No Qualifications found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ qualification }: IRootState) => ({
  qualificationList: qualification.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Qualification);
