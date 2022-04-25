<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="pfaApp.ferm.home.createOrEditLabel" data-cy="FermCreateUpdateHeading" v-text="$t('pfaApp.ferm.home.createOrEditLabel')">
          Create or edit a Ferm
        </h2>
        <div>
          <div class="form-group" v-if="ferm.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="ferm.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.ferm.numParcelle')" for="ferm-numParcelle">Num Parcelle</label>
            <input
              type="number"
              class="form-control"
              name="numParcelle"
              id="ferm-numParcelle"
              data-cy="numParcelle"
              :class="{ valid: !$v.ferm.numParcelle.$invalid, invalid: $v.ferm.numParcelle.$invalid }"
              v-model.number="$v.ferm.numParcelle.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.ferm.photo')" for="ferm-photo">Photo</label>
            <input
              type="text"
              class="form-control"
              name="photo"
              id="ferm-photo"
              data-cy="photo"
              :class="{ valid: !$v.ferm.photo.$invalid, invalid: $v.ferm.photo.$invalid }"
              v-model="$v.ferm.photo.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.ferm.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./ferm-update.component.ts"></script>
