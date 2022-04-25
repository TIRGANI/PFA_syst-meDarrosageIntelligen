<template>
  <div>
    <h2 id="page-heading" data-cy="BoitierHeading">
      <span v-text="$t('pfaApp.boitier.home.title')" id="boitier-heading">Boitiers</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('pfaApp.boitier.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'BoitierCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-boitier"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('pfaApp.boitier.home.createLabel')"> Create a new Boitier </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && boitiers && boitiers.length === 0">
      <span v-text="$t('pfaApp.boitier.home.notFound')">No boitiers found</span>
    </div>
    <div class="table-responsive" v-if="boitiers && boitiers.length > 0">
      <table class="table table-striped" aria-describedby="boitiers">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('pfaApp.boitier.reference')">Reference</span></th>
            <th scope="row"><span v-text="$t('pfaApp.boitier.type')">Type</span></th>
            <th scope="row"><span v-text="$t('pfaApp.boitier.alerte')">Alerte</span></th>
            <th scope="row"><span v-text="$t('pfaApp.boitier.brache')">Brache</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="boitier in boitiers" :key="boitier.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'BoitierView', params: { boitierId: boitier.id } }">{{ boitier.id }}</router-link>
            </td>
            <td>{{ boitier.reference }}</td>
            <td>{{ boitier.type }}</td>
            <td>
              <div v-if="boitier.alerte">
                <router-link :to="{ name: 'AlerteView', params: { alerteId: boitier.alerte.id } }">{{ boitier.alerte.id }}</router-link>
              </div>
            </td>
            <td>
              <span v-for="(brache, i) in boitier.braches" :key="brache.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'BracheView', params: { bracheId: brache.id } }">{{
                  brache.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'BoitierView', params: { boitierId: boitier.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'BoitierEdit', params: { boitierId: boitier.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(boitier)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="pfaApp.boitier.delete.question" data-cy="boitierDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-boitier-heading" v-text="$t('pfaApp.boitier.delete.question', { id: removeId })">
          Are you sure you want to delete this Boitier?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-boitier"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeBoitier()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./boitier.component.ts"></script>
