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

import { getEntities } from './producto.reducer';

export const Producto = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const productoList = useAppSelector(state => state.producto.entities);
  const loading = useAppSelector(state => state.producto.loading);
  const totalItems = useAppSelector(state => state.producto.totalItems);

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
      <h2 id="producto-heading" data-cy="ProductoHeading">
        <Translate contentKey="evergreenApp.producto.home.title">Productos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="evergreenApp.producto.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/producto/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="evergreenApp.producto.home.createLabel">Create new Producto</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {productoList && productoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="evergreenApp.producto.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('identificador')}>
                  <Translate contentKey="evergreenApp.producto.identificador">Identificador</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('identificador')} />
                </th>
                <th className="hand" onClick={sort('nombre')}>
                  <Translate contentKey="evergreenApp.producto.nombre">Nombre</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nombre')} />
                </th>
                <th className="hand" onClick={sort('descripcion')}>
                  <Translate contentKey="evergreenApp.producto.descripcion">Descripcion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('descripcion')} />
                </th>
                <th className="hand" onClick={sort('fechaElaboracion')}>
                  <Translate contentKey="evergreenApp.producto.fechaElaboracion">Fecha Elaboracion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fechaElaboracion')} />
                </th>
                <th className="hand" onClick={sort('lote')}>
                  <Translate contentKey="evergreenApp.producto.lote">Lote</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lote')} />
                </th>
                <th className="hand" onClick={sort('cantidad')}>
                  <Translate contentKey="evergreenApp.producto.cantidad">Cantidad</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cantidad')} />
                </th>
                <th className="hand" onClick={sort('unidadMedida')}>
                  <Translate contentKey="evergreenApp.producto.unidadMedida">Unidad Medida</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('unidadMedida')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productoList.map((producto, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/producto/${producto.id}`} color="link" size="sm">
                      {producto.id}
                    </Button>
                  </td>
                  <td>{producto.identificador}</td>
                  <td>{producto.nombre}</td>
                  <td>{producto.descripcion}</td>
                  <td>
                    {producto.fechaElaboracion ? (
                      <TextFormat type="date" value={producto.fechaElaboracion} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{producto.lote}</td>
                  <td>{producto.cantidad}</td>
                  <td>{producto.unidadMedida}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/producto/${producto.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/producto/${producto.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/producto/${producto.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="evergreenApp.producto.home.notFound">No Productos found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={productoList && productoList.length > 0 ? '' : 'd-none'}>
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

export default Producto;
