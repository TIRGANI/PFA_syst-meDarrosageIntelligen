<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.typeSol.home.createOrEditLabel"
          data-cy="TypeSolCreateUpdateHeading"
          v-text="$t('pfaApp.typeSol.home.createOrEditLabel')"
        >
          Create or edit a TypeSol
        </h2>
        <div>
          <div class="form-group" v-if="typeSol.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="typeSol.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.typeSol.libelle')" for="type-sol-libelle">Libelle</label>
            <input
              type="number"
              class="form-control"
              name="libelle"
              id="type-sol-libelle"
              data-cy="libelle"
              :class="{ valid: !$v.typeSol.libelle.$invalid, invalid: $v.typeSol.libelle.$invalid }"
              v-model.number="$v.typeSol.libelle.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.typeSol.type')" for="type-sol-type">Type</label>
            <input
              type="text"
              class="form-control"
              name="type"
              id="type-sol-type"
              data-cy="type"
              :class="{ valid: !$v.typeSol.type.$invalid, invalid: $v.typeSol.type.$invalid }"
              v-model="$v.typeSol.type.$model"
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
            :disabled="$v.typeSol.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./type-sol-update.component.ts"></script>
