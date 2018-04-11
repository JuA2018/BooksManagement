package org.techtown.booksmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteDBAdapterActivity extends BaseAdapter {

    ArrayList<NoteVO> notelist = new ArrayList<NoteVO>();
    Context mContext;

    public NoteDBAdapterActivity(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {//몇번 돌릴건지
        return notelist.size();
    }

    @Override
    public Object getItem(int position) {
        return notelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {//실제로 띄워줌
        LayoutInflater inflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.note_low, null);

        TextView textid = (TextView)view.findViewById(R.id.text_id);//view를 통해 note_row 데이터 갖고옴
        TextView textwriter = (TextView)view.findViewById(R.id.text_writer);
        TextView textbook = (TextView)view.findViewById(R.id.text_book);

        textid.setText(notelist.get(position).getId());
        textwriter.setText(notelist.get(position).getWriter());
        textbook.setText(notelist.get(position).getBook());

        return view;
    }
    public void addData(String id, String writer, String book){//중간저장소에 저장하는 메소드
        NoteVO vo = new NoteVO();

        vo.setId(id);
        vo.setWriter(writer);
        vo.setBook(book);

        notelist.add(vo);
    }

    public void removeData(String id){
        for(int i = 0;i < notelist.size();i++){
            if(notelist.get(i).getId().equals(id)){
                notelist.remove(i);
            }
        }
    }

    //내부클래스 구현
    public class NoteVO{   //DATA클래스 프로퍼티(중간저장소) 생성자는 처음에 생성할대 한번실행하는거고

        private String id, writer, book;

        public String getId() {return id;}

        public void setId(String id) {this.id = id;}

        public String getWriter() {return writer;}

        public void setWriter(String writer) {this.writer = writer;}

        public String getBook() {return book;}

        public void setBook(String book) {this.book = book;}

        /*
            코틀린에서 코딩 -> data class NoteVO
            (val id: String, val writer: String, val book: String)
         */
    }
}