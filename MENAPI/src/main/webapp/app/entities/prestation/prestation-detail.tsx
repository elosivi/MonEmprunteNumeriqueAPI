import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './prestation.reducer';

export const PrestationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const prestationEntity = useAppSelector(state => state.prestation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="prestationDetailsHeading">
          <Translate contentKey="menapiApp.prestation.detail.title">Prestation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.id}</dd>
          <dt>
            <span id="nomPrestation">
              <Translate contentKey="menapiApp.prestation.nomPrestation">Nom Prestation</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.nomPrestation}</dd>
          <dt>
            <span id="nomUtilisateur">
              <Translate contentKey="menapiApp.prestation.nomUtilisateur">Nom Utilisateur</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.nomUtilisateur}</dd>
          <dt>
            <span id="nomMission">
              <Translate contentKey="menapiApp.prestation.nomMission">Nom Mission</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.nomMission}</dd>
          <dt>
            <span id="nomClient">
              <Translate contentKey="menapiApp.prestation.nomClient">Nom Client</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.nomClient}</dd>
          <dt>
            <span id="ecUnite">
              <Translate contentKey="menapiApp.prestation.ecUnite">Ec Unite</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.ecUnite}</dd>
          <dt>
            <span id="ecMensuelle">
              <Translate contentKey="menapiApp.prestation.ecMensuelle">Ec Mensuelle</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.ecMensuelle}</dd>
          <dt>
            <span id="ecTotale">
              <Translate contentKey="menapiApp.prestation.ecTotale">Ec Totale</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.ecTotale}</dd>
          <dt>
            <span id="ecTransportMensuel">
              <Translate contentKey="menapiApp.prestation.ecTransportMensuel">Ec Transport Mensuel</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.ecTransportMensuel}</dd>
          <dt>
            <span id="ecFabMateriel">
              <Translate contentKey="menapiApp.prestation.ecFabMateriel">Ec Fab Materiel</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.ecFabMateriel}</dd>
          <dt>
            <span id="ecUtilMaterielMensuel">
              <Translate contentKey="menapiApp.prestation.ecUtilMaterielMensuel">Ec Util Materiel Mensuel</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.ecUtilMaterielMensuel}</dd>
          <dt>
            <span id="ecCommMensuel">
              <Translate contentKey="menapiApp.prestation.ecCommMensuel">Ec Comm Mensuel</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.ecCommMensuel}</dd>
          <dt>
            <span id="nbrProfils">
              <Translate contentKey="menapiApp.prestation.nbrProfils">Nbr Profils</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.nbrProfils}</dd>
          <dt>
            <span id="dureeMois">
              <Translate contentKey="menapiApp.prestation.dureeMois">Duree Mois</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.dureeMois}</dd>
          <dt>
            <span id="dateDebut">
              <Translate contentKey="menapiApp.prestation.dateDebut">Date Debut</Translate>
            </span>
          </dt>
          <dd>
            {prestationEntity.dateDebut ? (
              <TextFormat value={prestationEntity.dateDebut} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dateFin">
              <Translate contentKey="menapiApp.prestation.dateFin">Date Fin</Translate>
            </span>
          </dt>
          <dd>
            {prestationEntity.dateFin ? <TextFormat value={prestationEntity.dateFin} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="typePresta">
              <Translate contentKey="menapiApp.prestation.typePresta">Type Presta</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.typePresta}</dd>
          <dt>
            <span id="lieupresta">
              <Translate contentKey="menapiApp.prestation.lieupresta">Lieupresta</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.lieupresta}</dd>
          <dt>
            <span id="donneesSaisies">
              <Translate contentKey="menapiApp.prestation.donneesSaisies">Donnees Saisies</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.donneesSaisies ? 'true' : 'false'}</dd>
          <dt>
            <span id="donneesReperes">
              <Translate contentKey="menapiApp.prestation.donneesReperes">Donnees Reperes</Translate>
            </span>
          </dt>
          <dd>{prestationEntity.donneesReperes ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="menapiApp.prestation.prestationProfil">Prestation Profil</Translate>
          </dt>
          <dd>
            {prestationEntity.prestationProfils
              ? prestationEntity.prestationProfils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {prestationEntity.prestationProfils && i === prestationEntity.prestationProfils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.prestation.transportProfil">Transport Profil</Translate>
          </dt>
          <dd>
            {prestationEntity.transportProfils
              ? prestationEntity.transportProfils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {prestationEntity.transportProfils && i === prestationEntity.transportProfils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.prestation.deplacementProfil">Deplacement Profil</Translate>
          </dt>
          <dd>
            {prestationEntity.deplacementProfils
              ? prestationEntity.deplacementProfils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {prestationEntity.deplacementProfils && i === prestationEntity.deplacementProfils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.prestation.materielProfil">Materiel Profil</Translate>
          </dt>
          <dd>
            {prestationEntity.materielProfils
              ? prestationEntity.materielProfils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {prestationEntity.materielProfils && i === prestationEntity.materielProfils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/prestation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/prestation/${prestationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PrestationDetail;
