import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './transport-profil.reducer';

export const TransportProfilDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const transportProfilEntity = useAppSelector(state => state.transportProfil.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transportProfilDetailsHeading">
          <Translate contentKey="menapiApp.transportProfil.detail.title">TransportProfil</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transportProfilEntity.id}</dd>
          <dt>
            <span id="nbHebdo">
              <Translate contentKey="menapiApp.transportProfil.nbHebdo">Nb Hebdo</Translate>
            </span>
          </dt>
          <dd>{transportProfilEntity.nbHebdo}</dd>
          <dt>
            <span id="kmHebdo">
              <Translate contentKey="menapiApp.transportProfil.kmHebdo">Km Hebdo</Translate>
            </span>
          </dt>
          <dd>{transportProfilEntity.kmHebdo}</dd>
          <dt>
            <Translate contentKey="menapiApp.transportProfil.profil">Profil</Translate>
          </dt>
          <dd>
            {transportProfilEntity.profils
              ? transportProfilEntity.profils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {transportProfilEntity.profils && i === transportProfilEntity.profils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.transportProfil.prestation">Prestation</Translate>
          </dt>
          <dd>
            {transportProfilEntity.prestations
              ? transportProfilEntity.prestations.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {transportProfilEntity.prestations && i === transportProfilEntity.prestations.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.transportProfil.transport">Transport</Translate>
          </dt>
          <dd>
            {transportProfilEntity.transports
              ? transportProfilEntity.transports.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {transportProfilEntity.transports && i === transportProfilEntity.transports.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/transport-profil" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transport-profil/${transportProfilEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransportProfilDetail;
