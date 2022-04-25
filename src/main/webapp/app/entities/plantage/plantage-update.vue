<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.plantage.home.createOrEditLabel"
          data-cy="PlantageCreateUpdateHeading"
          v-text="$t('pfaApp.plantage.home.createOrEditLabel')"
        >
          Create or edit a Plantage
        </h2>
        <div>
          <div class="form-group" v-if="plantage.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="plantage.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.plantage.date')" for="plantage-date">Date</label>
            <input
              type="text"
              class="form-control"
              name="date"
              id="plantage-date"
              data-cy="date"
              :class="{ valid: !$v.plantage.date.$invalid, invalid: $v.plantage.date.$invalid }"
              v-model="$v.plantage.date.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.plantage.nbrPlate')" for="plantage-nbrPlate">Nbr Plate</label>
            <input
              type="number"
              class="form-control"
              name="nbrPlate"
              id="plantage-nbrPlate"
              data-cy="nbrPlate"
              :class="{ valid: !$v.plantage.nbrPlate.$invalid, invalid: $v.plantage.nbrPlate.$invalid }"
              v-model.number="$v.plantage.nbrPlate.$model"
            />
          </div>
          <div class="form-group">
            <label v-text="$t('pfaApp.plantage.plante')" for="plantage-plante">Plante</label>
            <select
              class="form-control"
              id="plantage-plantes"
              data-cy="plante"
              multiple
              name="plante"
              v-if="plantage.plantes !== undefined"
              v-model="plantage.plantes"
            >
              <option v-bind:value="getSelected(plantage.plantes, planteOption)" v-for="planteOption in plantes" :key="planteOption.id">
                {{ planteOption.id }}
              </option>
            </select>
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
            :disabled="$v.plantage.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./plantage-update.component.ts"></script>
