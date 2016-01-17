class CreateDocuments < ActiveRecord::Migration
  def change
    create_table :documents do |t|
      t.string :title
      t.binary :content
      t.timestamps null: false
    end
  end
end
