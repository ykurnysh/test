class DocumentsController < ApplicationController
    def edit
	if(session[:undo]==nil) then
    @document=Document.find_by title: params[:id]
    session[:title]=params[:id]
    if(@document==nil) then
	@document=Document.new
	@document.serialize
    else
	@document.deserialize
    end
    session[:undo]=[@document.content]
    session[:redo]=Array.new
	else
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	end
    end
	def create
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.title=session[:title]
        @document.save!
	session[:undo]=nil
	session[:redo]=nil
    redirect_to '/'
	end
    def index
	@documents=Document.all
    end
    def destroy
	@document = Document.find_by title: params[:id]
	@document.destroy
	redirect_to index
    end
    def new
	@document=nil
    end
    def bold
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.setBold(params[:a].to_i, params[:b].to_i)
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def italic
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.setItalic(params[:a].to_i, params[:b].to_i)
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def underlined
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.setUnderlined(params[:a].to_i, params[:b].to_i)
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def striked
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.setStriked(params[:a].to_i, params[:b].to_i)
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def alignment
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.setAlignment(params[:a].to_i, params[:b].to_i)
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def indent
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.setIndent(params[:a].to_f, params[:b].to_i)
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def insert
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.insert(params[:a], params[:b].to_i)
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def deletebefore
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.deletebefore(params[:a].to_i, params[:b].to_i)
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def deleteafter
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.deleteafter(params[:a].to_i, params[:b].to_i)
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def font
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def color
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def table
	@document=Document.new
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	@document.addTable(params[:a].to_i, params[:b].to_i, params[:c].to_i)
	@document.serialize
	session[:undo]=session[:undo]+[@document.content]
	session[:redo]=Array.new
	render 'edit'
    end
    def undoaction
	@document=Document.new
	session[:redo].insert(0, session[:undo][length-1])
	session[:undo].delete_at(session[:undo].length-1)
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	render 'edit'
    end
    def redoaction
	@document=Document.new
	session[:undo]=+session[:redo][0]
	session[:redo].delete_at(0)
	@document.content=session[:undo][session[:undo].length-1]
	@document.deserialize
	render 'edit'
    end
    def new_document
	session[:undo]=nil
	session[:redo]=nil
	redirect_to '/documents/'+params[:id]+'/edit'
    end
end
