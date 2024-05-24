import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './transport.reducer';

export const TransportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const transportEntity = useAppSelector(state => state.transport.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transportDetailsHeading">
          <Translate contentKey="menapiApp.transport.detail.title">Transport</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transportEntity.id}</dd>
          <dt>
            <span id="categorie">
              <Translate contentKey="menapiApp.transport.categorie">Categorie</Translate>
            </span>
          </dt>
          <dd>{transportEntity.categorie}</dd>
          <dt>
            <span id="typeMoteur">
              <Translate contentKey="menapiApp.transport.typeMoteur">Type Moteur</Translate>
            </span>
          </dt>
          <dd>{transportEntity.typeMoteur}</dd>
          <dt>
            <span id="feKm">
              <Translate contentKey="menapiApp.transport.feKm">Fe Km</Translate>
            </span>
          </dt>
          <dd>{transportEntity.feKm}</dd>
          <dt>
            <Translate contentKey="menapiApp.transport.transportProfil">Transport Profil</Translate>
          </dt>
          <dd>
            {transportEntity.transportProfils
              ? transportEntity.transportProfils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {transportEntity.transportProfils && i === transportEntity.transportProfils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.transport.deplacementProfil">Deplacement Profil</Translate>
          </dt>
          <dd>
            {transportEntity.deplacementProfils
              ? transportEntity.deplacementProfils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {transportEntity.deplacementProfils && i === transportEntity.deplacementProfils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/transport" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transport/${transportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransportDetail;
