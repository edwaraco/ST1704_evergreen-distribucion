import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './separacion.reducer';

export const Separacion = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const separacionList = useAppSelector(state => state.separacion.entities);
  const loading = useAppSelector(state => state.separacion.loading);
  const totalItems = useAppSelector(state => state.separacion.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

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
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="separacion-heading" data-cy="SeparacionHeading">
        <Translate contentKey="evergreenApp.separacion.home.title">Separacions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="evergreenApp.separacion.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/separacion/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="evergreenApp.separacion.home.createLabel">Create new Separacion</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {separacionList && separacionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="evergreenApp.separacion.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('identificador')}>
                  <Translate contentKey="evergreenApp.separacion.identificador">Identificador</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('identificador')} />
                </th>
                <th className="hand" onClick={sort('lote')}>
                  <Translate contentKey="evergreenApp.separacion.lote">Lote</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lote')} />
                </th>
                <th className="hand" onClick={sort('cantidad')}>
                  <Translate contentKey="evergreenApp.separacion.cantidad">Cantidad</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cantidad')} />
                </th>
                <th className="hand" onClick={sort('fechaRealizacion')}>
                  <Translate contentKey="evergreenApp.separacion.fechaRealizacion">Fecha Realizacion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fechaRealizacion')} />
                </th>
                <th className="hand" onClick={sort('ubicacion')}>
                  <Translate contentKey="evergreenApp.separacion.ubicacion">Ubicacion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ubicacion')} />
                </th>
                <th className="hand" onClick={sort('responsable')}>
                  <Translate contentKey="evergreenApp.separacion.responsable">Responsable</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('responsable')} />
                </th>
                <th className="hand" onClick={sort('observaciones')}>
                  <Translate contentKey="evergreenApp.separacion.observaciones">Observaciones</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('observaciones')} />
                </th>
                <th>
                  <Translate contentKey="evergreenApp.separacion.pedido">Pedido</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {separacionList.map((separacion, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/separacion/${separacion.id}`} color="link" size="sm">
                      {separacion.id}
                    </Button>
                  </td>
                  <td>{separacion.identificador}</td>
                  <td>{separacion.lote}</td>
                  <td>{separacion.cantidad}</td>
                  <td>
                    {separacion.fechaRealizacion ? (
                      <TextFormat type="date" value={separacion.fechaRealizacion} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{separacion.ubicacion}</td>
                  <td>{separacion.responsable}</td>
                  <td>{separacion.observaciones}</td>
                  <td>{separacion.pedido ? <Link to={`/pedido/${separacion.pedido.id}`}>{separacion.pedido.identificador}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/separacion/${separacion.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/separacion/${separacion.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/separacion/${separacion.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="evergreenApp.separacion.home.notFound">No Separacions found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={separacionList && separacionList.length > 0 ? '' : 'd-none'}>
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

export default Separacion;
