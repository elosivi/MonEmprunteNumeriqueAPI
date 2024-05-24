import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './donnees-references.reducer';

export const DonneesReferencesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const donneesReferencesEntity = useAppSelector(state => state.donneesReferences.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="donneesReferencesDetailsHeading">
          <Translate contentKey="menapiApp.donneesReferences.detail.title">DonneesReferences</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{donneesReferencesEntity.id}</dd>
          <dt>
            <span id="libelle">
              <Translate contentKey="menapiApp.donneesReferences.libelle">Libelle</Translate>
            </span>
          </dt>
          <dd>{donneesReferencesEntity.libelle}</dd>
          <dt>
            <span id="donneeReference">
              <Translate contentKey="menapiApp.donneesReferences.donneeReference">Donnee Reference</Translate>
            </span>
          </dt>
          <dd>{donneesReferencesEntity.donneeReference}</dd>
          <dt>
            <Translate contentKey="menapiApp.donneesReferences.unite">Unite</Translate>
          </dt>
          <dd>{donneesReferencesEntity.unite ? donneesReferencesEntity.unite.id : ''}</dd>
          <dt>
            <Translate contentKey="menapiApp.donneesReferences.temporalite">Temporalite</Translate>
          </dt>
          <dd>{donneesReferencesEntity.temporalite ? donneesReferencesEntity.temporalite.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/donnees-references" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/donnees-references/${donneesReferencesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DonneesReferencesDetail;
