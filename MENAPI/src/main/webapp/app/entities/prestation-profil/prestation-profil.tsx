import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { searchEntities, getEntities } from './prestation-profil.reducer';

export const PrestationProfil = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const prestationProfilList = useAppSelector(state => state.prestationProfil.entities);
  const loading = useAppSelector(state => state.prestationProfil.loading);
  const totalItems = useAppSelector(state => state.prestationProfil.totalItems);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    } else {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    }
  };

  const startSearching = e => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="prestation-profil-heading" data-cy="PrestationProfilHeading">
        <Translate contentKey="menapiApp.prestationProfil.home.title">Prestation Profils</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="menapiApp.prestationProfil.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/prestation-profil/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="menapiApp.prestationProfil.home.createLabel">Create new Prestation Profil</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('menapiApp.prestationProfil.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {prestationProfilList && prestationProfilList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="menapiApp.prestationProfil.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nbMoisPresta')}>
                  <Translate contentKey="menapiApp.prestationProfil.nbMoisPresta">Nb Mois Presta</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nbMoisPresta')} />
                </th>
                <th className="hand" onClick={sort('nbSemCongesEstime')}>
                  <Translate contentKey="menapiApp.prestationProfil.nbSemCongesEstime">Nb Sem Conges Estime</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nbSemCongesEstime')} />
                </th>
                <th className="hand" onClick={sort('dureeHebdo')}>
                  <Translate contentKey="menapiApp.prestationProfil.dureeHebdo">Duree Hebdo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dureeHebdo')} />
                </th>
                <th className="hand" onClick={sort('dureeTeletravail')}>
                  <Translate contentKey="menapiApp.prestationProfil.dureeTeletravail">Duree Teletravail</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dureeTeletravail')} />
                </th>
                <th className="hand" onClick={sort('dureeReuAudio')}>
                  <Translate contentKey="menapiApp.prestationProfil.dureeReuAudio">Duree Reu Audio</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dureeReuAudio')} />
                </th>
                <th className="hand" onClick={sort('dureeReuVisio')}>
                  <Translate contentKey="menapiApp.prestationProfil.dureeReuVisio">Duree Reu Visio</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dureeReuVisio')} />
                </th>
                <th className="hand" onClick={sort('nbMailsSansPJ')}>
                  <Translate contentKey="menapiApp.prestationProfil.nbMailsSansPJ">Nb Mails Sans PJ</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nbMailsSansPJ')} />
                </th>
                <th className="hand" onClick={sort('nbMailsAvecPJ')}>
                  <Translate contentKey="menapiApp.prestationProfil.nbMailsAvecPJ">Nb Mails Avec PJ</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nbMailsAvecPJ')} />
                </th>
                <th className="hand" onClick={sort('veillePause')}>
                  <Translate contentKey="menapiApp.prestationProfil.veillePause">Veille Pause</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('veillePause')} />
                </th>
                <th className="hand" onClick={sort('veilleSoir')}>
                  <Translate contentKey="menapiApp.prestationProfil.veilleSoir">Veille Soir</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('veilleSoir')} />
                </th>
                <th className="hand" onClick={sort('veilleWeekend')}>
                  <Translate contentKey="menapiApp.prestationProfil.veilleWeekend">Veille Weekend</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('veilleWeekend')} />
                </th>
                <th className="hand" onClick={sort('nbTerminaux')}>
                  <Translate contentKey="menapiApp.prestationProfil.nbTerminaux">Nb Terminaux</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nbTerminaux')} />
                </th>
                <th className="hand" onClick={sort('nbDeplacements')}>
                  <Translate contentKey="menapiApp.prestationProfil.nbDeplacements">Nb Deplacements</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nbDeplacements')} />
                </th>
                <th className="hand" onClick={sort('ecMensuelle')}>
                  <Translate contentKey="menapiApp.prestationProfil.ecMensuelle">Ec Mensuelle</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecMensuelle')} />
                </th>
                <th className="hand" onClick={sort('ecTotalePreta')}>
                  <Translate contentKey="menapiApp.prestationProfil.ecTotalePreta">Ec Totale Preta</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecTotalePreta')} />
                </th>
                <th className="hand" onClick={sort('ecTransportMensuel')}>
                  <Translate contentKey="menapiApp.prestationProfil.ecTransportMensuel">Ec Transport Mensuel</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecTransportMensuel')} />
                </th>
                <th className="hand" onClick={sort('ecFabMateriel')}>
                  <Translate contentKey="menapiApp.prestationProfil.ecFabMateriel">Ec Fab Materiel</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecFabMateriel')} />
                </th>
                <th className="hand" onClick={sort('ecUtilMaterielMensuel')}>
                  <Translate contentKey="menapiApp.prestationProfil.ecUtilMaterielMensuel">Ec Util Materiel Mensuel</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecUtilMaterielMensuel')} />
                </th>
                <th className="hand" onClick={sort('ecCommMensuel')}>
                  <Translate contentKey="menapiApp.prestationProfil.ecCommMensuel">Ec Comm Mensuel</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecCommMensuel')} />
                </th>
                <th>
                  <Translate contentKey="menapiApp.prestationProfil.profil">Profil</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {prestationProfilList.map((prestationProfil, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/prestation-profil/${prestationProfil.id}`} color="link" size="sm">
                      {prestationProfil.id}
                    </Button>
                  </td>
                  <td>{prestationProfil.nbMoisPresta}</td>
                  <td>{prestationProfil.nbSemCongesEstime}</td>
                  <td>{prestationProfil.dureeHebdo}</td>
                  <td>{prestationProfil.dureeTeletravail}</td>
                  <td>{prestationProfil.dureeReuAudio}</td>
                  <td>{prestationProfil.dureeReuVisio}</td>
                  <td>{prestationProfil.nbMailsSansPJ}</td>
                  <td>{prestationProfil.nbMailsAvecPJ}</td>
                  <td>{prestationProfil.veillePause ? 'true' : 'false'}</td>
                  <td>{prestationProfil.veilleSoir ? 'true' : 'false'}</td>
                  <td>{prestationProfil.veilleWeekend ? 'true' : 'false'}</td>
                  <td>{prestationProfil.nbTerminaux}</td>
                  <td>{prestationProfil.nbDeplacements}</td>
                  <td>{prestationProfil.ecMensuelle}</td>
                  <td>{prestationProfil.ecTotalePreta}</td>
                  <td>{prestationProfil.ecTransportMensuel}</td>
                  <td>{prestationProfil.ecFabMateriel}</td>
                  <td>{prestationProfil.ecUtilMaterielMensuel}</td>
                  <td>{prestationProfil.ecCommMensuel}</td>
                  <td>
                    {prestationProfil.profil ? <Link to={`/profil/${prestationProfil.profil.id}`}>{prestationProfil.profil.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/prestation-profil/${prestationProfil.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/prestation-profil/${prestationProfil.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/prestation-profil/${prestationProfil.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="menapiApp.prestationProfil.home.notFound">No Prestation Profils found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={prestationProfilList && prestationProfilList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default PrestationProfil;
