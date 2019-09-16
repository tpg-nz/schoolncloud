import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './requirement.reducer';
import { IRequirement } from 'app/shared/model/requirement.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRequirementProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Requirement extends React.Component<IRequirementProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { requirementList, match } = this.props;
    return (
      <div>
        <h2 id="requirement-heading">
          <Translate contentKey="catalogApp.requirement.home.title">Requirements</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="catalogApp.requirement.home.createLabel">Create new Requirement</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {requirementList && requirementList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.requirement.guid">Guid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.requirement.level">Level</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.requirement.subject">Subject</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {requirementList.map((requirement, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${requirement.id}`} color="link" size="sm">
                        {requirement.id}
                      </Button>
                    </td>
                    <td>{requirement.guid}</td>
                    <td>{requirement.level}</td>
                    <td>{requirement.subject ? <Link to={`subject/${requirement.subject.id}`}>{requirement.subject.guid}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${requirement.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${requirement.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${requirement.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="catalogApp.requirement.home.notFound">No Requirements found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ requirement }: IRootState) => ({
  requirementList: requirement.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Requirement);
