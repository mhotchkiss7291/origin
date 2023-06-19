
ActiveRecord::Schema[7.0].define(version: 2023_06_19_195903) do
  create_table "articles", force: :cascade do |t|
    t.string "title"
    t.text "body"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

end
