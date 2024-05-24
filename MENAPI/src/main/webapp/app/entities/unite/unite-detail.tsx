import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './unite.reducer';

export const UniteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const uniteEntity = useAppSelector(state => state.unite.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="uniteDetailsHeading">
          <Translate contentKey="menapiApp.unite.detail.title">Unite</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{uniteEntity.id}</dd>
          <dt>
            <span id="libelle">
              <Translate contentKey="menapiApp.unite.libelle">Libelle</Translate>
            </span>
          </dt>
          <dd>{uniteEntity.libelle}</dd>
          <dt>
            <span id="estTemporelle">
              <Translate contentKey="menapiApp.unite.estTemporelle">Est Temporelle</Translate>
            </span>
          </dt>
          <dd>{uniteEntity.estTemporelle ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/unite" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/unite/${uniteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UniteDetail;
